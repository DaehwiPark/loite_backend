package com.boot.loiteBackend.web.social.service;

import com.boot.loiteBackend.web.social.dto.SocialUserRegistrationDto;
import com.boot.loiteBackend.web.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialRegisterService {

    private final UserRepository userRepository;
    private final SocialUserRepository socialUserRepository;
    private final TokenService tokenService;

    public LoginResponseDto register(
            String providerName,
            SocialUserRegistrationDto dto,
            HttpServletResponse response,
            String userLoginType
    ) {
        ProviderType provider = parseProvider(providerName);

        // 이메일 중복 확인
        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            throw new CustomException(SocialErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER);
        }

        // 사용자 계정 등록
        UserEntity user = UserEntity.builder()
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .userRegisterType(provider.name())   // 가입 방식
                .userStatus("ACTIVE")
                .userRole("USER")
                .emailVerified(true)
                .emailVerifiedAt(LocalDateTime.now())
                .isOver14(dto.isOver14())
                .agreeTerms(dto.isAgreeTerms())
                .agreePrivacy(dto.isAgreePrivacy())
                .agreeMarketingSns(dto.isAgreeMarketingSns())
                .agreeMarketingEmail(dto.isAgreeMarketingEmail())
                .build();

        userRepository.save(user);

        // 소셜 계정 연동 정보 저장
        SocialUserEntity social = SocialUserEntity.builder()
                .user(user)
                .socialType(provider.name())
                .socialNumber(dto.getSocialId())
                .socialEmail(dto.getUserEmail())
                .socialUserName(dto.getUserName())
                .connectedAt(LocalDateTime.now())
                .build();

        socialUserRepository.save(social);

        // 로그인 토큰 발급
        return tokenService.getLoginToken(user, response, userLoginType);
    }

    private ProviderType parseProvider(String providerName) {
        try {
            return ProviderType.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(SocialErrorCode.UNSUPPORTED_PROVIDER);
        }
    }
}
