package com.boot.loiteBackend.web.social.service;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.handler.OAuthHandler;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
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
    private final SocialUserRepository socialUserRepository;
    private final TokenService tokenService;

    public ApiResponse<LoginResponseDto> login(String provider, String code, HttpServletResponse response) {
        OAuthHandler handler = resolver.resolveLogin(provider);
        String accessToken = handler.requestAccessToken(code);
        OAuthUserInfo userInfo = handler.getUserInfo(accessToken);

        String email = userInfo.getEmail();
        String socialId = userInfo.getSocialId();
        String name = userInfo.getName();
        String userLoginType = provider.toUpperCase();

        // 소셜 계정으로 이미 연동된 계정이 있는 경우
        Optional<SocialUserEntity> socialOpt = socialUserRepository
                .findBySocialTypeAndSocialNumber(userLoginType, socialId);

        if (socialOpt.isPresent()) {
            UserEntity user = socialOpt.get().getUser();
            LoginResponseDto loginDto = tokenService.getLoginToken(user, response, userLoginType);
            return ApiResponse.ok(loginDto, userLoginType + " 로그인 성공 (연동 계정)");
        }

        // 기존 이메일 회원이 존재하는 경우 -> 연동 정보 저장 후 로그인 처리
        Optional<UserEntity> userOpt = userRepository.findByUserEmail(email);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            SocialUserEntity userSocial = SocialUserEntity.builder()
                    .user(user)
                    .socialEmail(email)
                    .socialNumber(socialId)
                    .socialType(userLoginType)
                    .socialUserName(name)
                    .build();

            socialUserRepository.save(userSocial);

            LoginResponseDto loginDto = tokenService.getLoginToken(user, response, userLoginType);
            return ApiResponse.ok(loginDto, userLoginType + " 로그인 성공 (연동 처리)");
        }

        // 신규 유저 → 회원가입 유도
        log.info("신규 회원 가입 유도: email={}, provider={}, socialId={}, name={}", email, provider, socialId, name);

        return ApiResponse.error(
                SocialErrorCode.USER_NOT_REGISTERED,
                Map.of(
                        "email", email,
                        "socialId", socialId,
                        "name", name,
                        "provider", userLoginType
                )
        );
    }
}
