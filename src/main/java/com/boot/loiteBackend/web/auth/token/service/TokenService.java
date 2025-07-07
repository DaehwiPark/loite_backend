package com.boot.loiteBackend.web.auth.token.service;

import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.token.dto.TokenRequestDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface TokenService {

    void saveRefreshToken(String userId, String token, long expirationSeconds);

    Optional<String> getRefreshToken(String userId);

    void deleteRefreshToken(String userId);

    void extendRefreshTokenTTL(String userId, String refreshToken);

    Long getRemainingTTL(String userId);

    LoginResponseDto refresh(TokenRequestDto dto, HttpServletResponse response);

    LoginResponseDto getLoginToken(UserEntity user, HttpServletResponse response);
}
