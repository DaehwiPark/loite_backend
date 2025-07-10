package com.boot.loiteBackend.web.social.service;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.dto.SocialLinkingDto;
import com.boot.loiteBackend.web.social.dto.SocialLinkingStatusResponseDto;
import com.boot.loiteBackend.web.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.handler.OAuthHandler;
import com.boot.loiteBackend.web.social.handler.OAuthLinkHandler;
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


    public ApiResponse<LoginResponseDto> link(String provider, String code, CustomUserDetails loginUser, HttpServletResponse response) {
        OAuthLinkHandler handler = resolver.resolveLink(provider);
        String accessToken = handler.requestLinkingAccessToken(code);
        OAuthUserInfoDto userInfo = handler.getUserInfo(accessToken);

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

        return ApiResponse.ok(tokenService.getLoginToken(user, response),
                provider.toUpperCase() + " 계정 연동 완료");
    }

    public ApiResponse<String> unlinkAccount(String provider, CustomUserDetails loginUser) {
        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        Optional<SocialUserEntity> socialUserOpt = socialUserRepository
                .findByUserAndSocialType(user, provider.toUpperCase());

        if (socialUserOpt.isPresent()) {
            socialUserRepository.delete(socialUserOpt.get());
            return ApiResponse.ok(provider.toUpperCase() + " 연동이 성공적으로 해제되었습니다.");
        } else {
            return ApiResponse.error(SocialErrorCode.SOCIAL_NOT_LINKED);
        }
    }
}
