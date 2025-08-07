package com.boot.loiteBackend.web.social.service;

import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.dto.SocialVerificationResultDto;
import com.boot.loiteBackend.web.social.handler.OAuthUnLinkHandlers;
import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.dto.SocialLinkingDto;
import com.boot.loiteBackend.web.social.dto.SocialLinkingStatusResponseDto;
import com.boot.loiteBackend.domain.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.handler.OAuthLinkHandler;
import com.boot.loiteBackend.web.social.handler.OAuthVerifyHandlers;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import com.boot.loiteBackend.web.user.general.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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


    public LoginResponseDto link(String provider, String code, CustomUserDetails loginUser, HttpServletResponse response, String userLoginType) {
        OAuthLinkHandler handler = resolver.resolveLink(provider);
        String accessToken = handler.requestLinkingAccessToken(code);
        OAuthUserInfo userInfo = handler.getUserInfo(accessToken);

        String email = userInfo.getEmail();
        String socialId = userInfo.getSocialId();

        Optional<SocialUserEntity> existingSocialOpt = socialUserRepository
                .findBySocialTypeAndSocialNumber(provider.toUpperCase(), socialId);

        if (existingSocialOpt.isPresent()) {
            if (!existingSocialOpt.get().getUser().getUserId().equals(loginUser.getUserId())) {
                throw new CustomException(SocialErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER);
            } else {
                throw new CustomException(SocialErrorCode.ALREADY_LINKED);
            }
        }

        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new CustomException(SocialErrorCode.USER_NOT_FOUND));

        SocialUserEntity socialUser = SocialUserEntity.builder()
                .user(user)
                .socialType(provider.toUpperCase())
                .socialNumber(socialId)
                .socialEmail(email)
                .socialUserName(userInfo.getName())
                .connectedAt(LocalDateTime.now())
                .build();

        socialUserRepository.save(socialUser);

        return tokenService.getLoginToken(user, response, userLoginType);
    }

    @Transactional
    public void unlinkAccount(String provider, CustomUserDetails loginUser, String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new CustomException(SocialErrorCode.INVALID_SOCIAL_TOKEN);
        }

        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new CustomException(SocialErrorCode.USER_NOT_FOUND));

        SocialUserEntity socialUser = socialUserRepository.findByUserAndSocialType(user, provider.toUpperCase())
                .orElseThrow(() -> new CustomException(SocialErrorCode.SOCIAL_NOT_LINKED));

        OAuthUnLinkHandlers handler = resolver.resolveUnlink(provider);

        try {
            handler.unlinkSocialAccount(accessToken, socialUser.getSocialEmail());
        } catch (CustomException e) {
            throw e; // 인증 실패 등
        } catch (Exception e) {
            throw new CustomException(SocialErrorCode.UNLINK_FAILED);
        }

        socialUserRepository.delete(socialUser);
    }

    public SocialVerificationResultDto verifySocialAuthentication(String provider, String code, CustomUserDetails loginUser) {
        OAuthVerifyHandlers handler = resolver.resolveVerify(provider);
        String accessToken = handler.requestVerifyAccessToken(code);
        OAuthUserInfo userInfo = handler.getUserInfo(accessToken);

        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new CustomException(SocialErrorCode.USER_NOT_FOUND));

        SocialUserEntity linked = socialUserRepository.findByUserAndSocialType(user, provider.toUpperCase())
                .orElseThrow(() -> new CustomException(SocialErrorCode.SOCIAL_VERIFICATION_FAILED));

        boolean emailMatches = linked.getSocialEmail().equalsIgnoreCase(userInfo.getEmail());
        boolean socialNumberMatches = linked.getSocialNumber().equals(userInfo.getSocialId());

        if (!emailMatches || !socialNumberMatches) {
            throw new CustomException(SocialErrorCode.SOCIAL_VERIFICATION_FAILED);
        }

        return new SocialVerificationResultDto(true, accessToken);
    }
}
