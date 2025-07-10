package com.boot.loiteBackend.domain.token.service;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.dto.TokenRequestDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface TokenService {

    void saveRefreshToken(String userId, String token, long expirationSeconds);

    Optional<String> getRefreshToken(String userId);

    void deleteRefreshToken(String userId);

    void extendRefreshTokenTTL(String userId, String refreshToken);

    LoginResponseDto refresh(TokenRequestDto dto, HttpServletResponse response);

    LoginResponseDto getLoginToken(UserEntity user, HttpServletResponse response, String userLoginType);
}
