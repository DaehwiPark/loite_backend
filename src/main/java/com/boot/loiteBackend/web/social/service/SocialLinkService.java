package com.boot.loiteBackend.web.social.service;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.handler.OAuthHandler;
import com.boot.loiteBackend.web.social.handler.OAuthLinkHandler;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialLinkService {

    private final OAuthHandlerResolver resolver;
    private final SocialUserRepository socialUserRepository;
    private final TokenService tokenService;

    public ApiResponse<LoginResponseDto> link(String provider, String code, UserEntity loginUser, HttpServletResponse response) {
        OAuthLinkHandler handler = resolver.resolveLink(provider);
        String accessToken = handler.requestAccessToken(code);
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

        // 연동 처리
        SocialUserEntity socialUser = SocialUserEntity.builder()
                .user(loginUser)
                .socialType(provider.toUpperCase())
                .socialNumber(socialId)
                .connectedAt(LocalDateTime.now())
                .build();

        socialUserRepository.save(socialUser);

        return ApiResponse.ok(tokenService.getLoginToken(loginUser, response),
                provider.toUpperCase() + " 계정 연동 완료");
    }

}
