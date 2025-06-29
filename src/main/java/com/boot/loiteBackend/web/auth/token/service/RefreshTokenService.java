package com.boot.loiteBackend.web.auth.token.service;

import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RedisTemplate<String, String> redisTemplate;
  private final JwtTokenProvider jwtTokenProvider;

  // Redis에 RefreshToken 저장 (TTL 초 단위)
  public void saveRefreshToken(String userId, String token, long expirationSeconds) {
    String key = "refreshToken:" + userId;
    redisTemplate.opsForValue().set(key, token, expirationSeconds, TimeUnit.SECONDS);
  }

  // Redis에서 RefreshToken 조회
  public Optional<String> getRefreshToken(String userId) {
    String key = "refreshToken:" + userId;
    return Optional.ofNullable(redisTemplate.opsForValue().get(key));
  }

  // Redis에서 RefreshToken 삭제
  public void deleteRefreshToken(String userId) {
    String key = "refreshToken:" + userId;
    redisTemplate.delete(key);
  }

  // Redis에 저장된 RefreshToken TTL 연장
  public void extendRefreshTokenTTL(String userId, String refreshToken) {
    long expirationSeconds = jwtTokenProvider.getRefreshTokenValidity() / 1000;
    String key = "refreshToken:" + userId;
    redisTemplate.opsForValue().set(key, refreshToken, expirationSeconds, TimeUnit.SECONDS);
  }

  // 남은 TTL 조회 (디버깅 또는 로깅용)
  public Long getRemainingTTL(String userId) {
    String key = "refreshToken:" + userId;
    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }
}