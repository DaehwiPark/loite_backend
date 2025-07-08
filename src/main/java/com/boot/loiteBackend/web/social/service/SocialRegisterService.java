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

    public LoginResponseDto register(String providerName, SocialUserRegistrationDto dto, HttpServletResponse response) {
        ProviderType provider = parseProvider(providerName);

        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            throw new CustomException(SocialErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER);
        }

        UserEntity user = UserEntity.builder()
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .isOver14(dto.isOver14())
                .agreeTerms(dto.isAgreeTerms())
                .agreePrivacy(dto.isAgreePrivacy())
                .agreeMarketingSns(dto.isAgreeMarketingSns())
                .agreeMarketingEmail(dto.isAgreeMarketingEmail())
                .emailVerified(true)
                .emailVerifiedAt(LocalDateTime.now())
                .userRole("USER")
                .userStatus("ACTIVE")
                .userRegisterType(provider.name())
                .build();

        userRepository.save(user);

        SocialUserEntity social = SocialUserEntity.builder()
                .user(user)
                .socialType(provider.name())
                .socialNumber(dto.getSocialId())
                .connectedAt(LocalDateTime.now())
                .socialEmail(dto.getUserEmail())
                .socialUserName(dto.getUserName())
                .build();

        socialUserRepository.save(social);

        return tokenService.getLoginToken(user, response);
    }

    private ProviderType parseProvider(String providerName) {
        try {
            return ProviderType.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(SocialErrorCode.UNSUPPORTED_PROVIDER);
        }
    }
}
