package com.boot.loiteBackend.web.social.service;

import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.handler.OAuthHandler;
import com.boot.loiteBackend.web.social.link.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final OAuthHandlerResolver resolver;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public ApiResponse<LoginResponseDto> login(String provider, String code, HttpServletResponse response) {
        OAuthHandler handler = resolver.resolveLogin(provider);
        String accessToken = handler.requestAccessToken(code);
        OAuthUserInfoDto userInfo = handler.getUserInfo(accessToken);

        String email = userInfo.getEmail();
        String socialId = userInfo.getSocialId();
        String name = userInfo.getName();

        Optional<UserEntity> userOpt = userRepository.findByUserEmail(email);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            boolean linked = user.getUserSocials().stream()
                    .anyMatch(s -> s.getSocialType().equalsIgnoreCase(provider) &&
                            s.getSocialNumber().equals(socialId));

            if (!linked) {
                return ApiResponse.error(
                        SocialErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER,
                        Map.of(
                                "email", email,
                                "socialId", socialId,
                                "provider", provider
                        )
                );
            }

            LoginResponseDto loginDto = tokenService.getLoginToken(user, response);
            return ApiResponse.ok(loginDto, provider.toUpperCase() + " 로그인 성공");
        }

        log.info("신규 회원 가입 유도: email={}, provider={}, socialId={}, name={}", email, provider, socialId, name);

        return ApiResponse.error(
                SocialErrorCode.USER_NOT_REGISTERED,
                Map.of(
                        "email", email,
                        "socialId", socialId,
                        "name", name,
                        "provider", provider
                )
        );
    }
}