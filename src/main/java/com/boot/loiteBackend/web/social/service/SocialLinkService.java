package com.boot.loiteBackend.web.social.service;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.dto.SocialVerificationResultDto;
import com.boot.loiteBackend.web.social.handler.OAuthUnLinkHandlers;
import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.dto.SocialLinkingDto;
import com.boot.loiteBackend.web.social.dto.SocialLinkingStatusResponseDto;
import com.boot.loiteBackend.web.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.handler.OAuthLinkHandler;
import com.boot.loiteBackend.web.social.handler.OAuthVerifyHandlers;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialLinkService {

    private final OAuthHandlerResolver resolver;
    private final SocialUserRepository socialUserRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SocialLinkingStatusResponseDto getSocialLinkingStatus(CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        List<SocialUserEntity> linkedList = socialUserRepository.findAllByUser(user);

        List<SocialLinkingDto> linkingDtos = linkedList.stream()
                .map(linked -> SocialLinkingDto.builder()
                        .socialType(linked.getSocialType())
                        .email(linked.getSocialEmail())
                        .name(linked.getSocialUserName())
                        .connectedAt(linked.getConnectedAt())
                        .isRegisterAccount(linked.getSocialType().equalsIgnoreCase(userDetails.getUserRegisterType()))
                        .build())
                .collect(Collectors.toList());

        return SocialLinkingStatusResponseDto.builder()
                .userRegisterType(userDetails.getUserRegisterType())
                .socialLinking(linkingDtos)
                .build();
    }


    public ApiResponse<LoginResponseDto> link(String provider, String code, CustomUserDetails loginUser, HttpServletResponse response, String userLoginType) {
        OAuthLinkHandler handler = resolver.resolveLink(provider);
        String accessToken = handler.requestLinkingAccessToken(code);
        OAuthUserInfo userInfo = handler.getUserInfo(accessToken);

        String email = userInfo.getEmail();
        String socialId = userInfo.getSocialId();

        // 소셜 계정이 이미 다른 사용자에 의해 연동되었는지 확인
        Optional<SocialUserEntity> existingSocialOpt = socialUserRepository
                .findBySocialTypeAndSocialNumber(provider.toUpperCase(), socialId);

        if (existingSocialOpt.isPresent()) {
            if (!existingSocialOpt.get().getUser().getUserId().equals(loginUser.getUserId())) {
                return ApiResponse.error(
                        SocialErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER,
                        Map.of("email", email, "provider", provider)
                );
            } else {
                return ApiResponse.error(
                        SocialErrorCode.ALREADY_LINKED,
                        Map.of("email", email, "provider", provider)
                );
            }
        }
        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        SocialUserEntity socialUser = SocialUserEntity.builder()
                .user(user)
                .socialType(provider.toUpperCase())
                .socialNumber(socialId)
                .socialEmail(userInfo.getEmail())
                .socialUserName(userInfo.getName())
                .connectedAt(LocalDateTime.now())
                .build();

        socialUserRepository.save(socialUser);

        return ApiResponse.ok(tokenService.getLoginToken(user, response, userLoginType),
                provider.toUpperCase() + " 계정 연동 완료");
    }


    public ApiResponse<String> unlinkAccount(String provider, CustomUserDetails loginUser, String accessToken) {
        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        // 연동된 소셜 계정 확인
        Optional<SocialUserEntity> socialUserOpt = socialUserRepository
                .findByUserAndSocialType(user, provider.toUpperCase());

        if (socialUserOpt.isEmpty()) {
            return ApiResponse.error(SocialErrorCode.SOCIAL_NOT_LINKED);
        }

        SocialUserEntity socialUser = socialUserOpt.get();

        try {
            // accessToken 사용하여 플랫폼 unlink API 호출
            OAuthUnLinkHandlers handler = resolver.resolveUnlink(provider);
            handler.unlinkSocialAccount(accessToken, socialUser.getSocialEmail());

        } catch (Exception e) {
            return ApiResponse.error(SocialErrorCode.UNLINK_FAILED, "플랫폼 연동 해제 실패: " + e.getMessage());
        }

        // 연동 정보 삭제
        socialUserRepository.delete(socialUser);
        return ApiResponse.ok(provider.toUpperCase() + " 연동이 성공적으로 해제되었습니다.");
    }



    public ApiResponse<SocialVerificationResultDto> verifySocialAuthentication(String provider, String code, CustomUserDetails loginUser) {
        // 인증용 핸들러 resolve
        OAuthVerifyHandlers handler = resolver.resolveVerify(provider);

        // 인증 코드로 access token 요청
        String accessToken = handler.requestVerifyAccessToken(code);

        // access token으로 사용자 정보 요청
        OAuthUserInfo userInfo = handler.getUserInfo(accessToken);
        String authenticatedEmail = userInfo.getEmail();
        String authenticatedSocialNumber = userInfo.getSocialId();

        // 현재 로그인된 사용자 기준 연동된 소셜 계정 조회
        Optional<SocialUserEntity> linkedOpt = socialUserRepository.findByUserAndSocialType(
                userRepository.findById(loginUser.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다.")),
                provider.toUpperCase()
        );

        boolean verified = false;

        if (linkedOpt.isPresent()) {
            SocialUserEntity linked = linkedOpt.get();
            boolean emailMatches = linked.getSocialEmail().equalsIgnoreCase(authenticatedEmail);
            boolean socialNumberMatches = linked.getSocialNumber().equals(authenticatedSocialNumber);
            verified = emailMatches && socialNumberMatches;
        }

        return ApiResponse.ok(
                new SocialVerificationResultDto(verified, accessToken),
                "소셜 인증 비교 결과 반환"
        );
    }

}
