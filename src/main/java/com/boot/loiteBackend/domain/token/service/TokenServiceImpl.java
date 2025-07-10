package com.boot.loiteBackend.domain.token.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.dto.TokenRequestDto;
import com.boot.loiteBackend.domain.token.error.TokenErrorCode;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

  private final RedisTemplate<String, String> redisTemplate;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtCookieUtil jwtCookieUtil;
  private final UserRepository userRepository;

  @Override
  public void saveRefreshToken(String userId, String token, long expirationSeconds) {
    redisTemplate.opsForValue().set("refreshToken:" + userId, token, expirationSeconds, TimeUnit.SECONDS);
  }

  @Override
  public Optional<String> getRefreshToken(String userId) {
    return Optional.ofNullable(redisTemplate.opsForValue().get("refreshToken:" + userId));
  }

  @Override
  public void deleteRefreshToken(String userId) {
    redisTemplate.delete("refreshToken:" + userId);
  }

  @Override
  public void extendRefreshTokenTTL(String userId, String refreshToken) {
    redisTemplate.opsForValue().set(
            "refreshToken:" + userId,
            refreshToken,
            jwtTokenProvider.getRefreshTokenValidity() / 1000,
            TimeUnit.SECONDS
    );
  }

  @Override
  public Long getRemainingTTL(String userId) {
    return redisTemplate.getExpire("refreshToken:" + userId, TimeUnit.SECONDS);
  }

  @Override
  public LoginResponseDto refresh(TokenRequestDto dto, HttpServletResponse response) {
    Long userId = jwtTokenProvider.getUserId(dto.getRefreshToken());
    String key = String.valueOf(userId);

    String storedToken = getRefreshToken(key)
            .orElseThrow(() -> new CustomException(TokenErrorCode.NOT_FOUND));

    if (!storedToken.equals(dto.getRefreshToken())) {
      throw new CustomException(TokenErrorCode.MISMATCH);
    }

    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(TokenErrorCode.NOT_FOUND));

    // 기존 refreshToken에 들어있던 로그인 타입 추출
    String userLoginType = jwtTokenProvider.getUserLoginType(dto.getRefreshToken());

    String newAccessToken = jwtTokenProvider.createToken(
            user.getUserId(),
            user.getUserEmail(),
            user.getUserRole(),
            user.getUserRegisterType(),
            userLoginType
    );

    extendRefreshTokenTTL(key, dto.getRefreshToken());

    jwtCookieUtil.addAccessTokenCookie(response, newAccessToken, jwtTokenProvider.getAccessTokenValidity() / 1000);

    return LoginResponseDto.builder()
            .refreshToken(dto.getRefreshToken())
            .tokenType("Bearer")
            .build();
  }

  @Override
  public LoginResponseDto getLoginToken(UserEntity user, HttpServletResponse response, String userLoginType) {
    // 1. AccessToken 발급 (로그인 방식 포함)
    String accessToken = jwtTokenProvider.createToken(
            user.getUserId(),
            user.getUserEmail(),
            user.getUserRole(),
            user.getUserRegisterType(),
            userLoginType
    );

    // RefreshToken 발급
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

    // Redis 저장
    String key = String.valueOf(user.getUserId());
    long expirationSeconds = jwtTokenProvider.getRefreshTokenValidity() / 1000;
    saveRefreshToken(key, refreshToken, expirationSeconds);

    // AccessToken 쿠키 저장
    jwtCookieUtil.addAccessTokenCookie(response, accessToken, jwtTokenProvider.getAccessTokenValidity() / 1000);

    // 응답 DTO
    return LoginResponseDto.builder()
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .build();
  }
}
