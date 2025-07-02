package com.boot.loiteBackend.web.auth.token.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.token.dto.TokenRequestDto;
import com.boot.loiteBackend.web.auth.token.error.TokenErrorCode;
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

    String newAccessToken = jwtTokenProvider.createToken(
            user.getUserId(),
            user.getUserEmail(),
            user.getRole()
    );

    extendRefreshTokenTTL(key, dto.getRefreshToken());

    jwtCookieUtil.addAccessTokenCookie(response, newAccessToken, jwtTokenProvider.getAccessTokenValidity() / 1000);

    return LoginResponseDto.builder()
            .refreshToken(dto.getRefreshToken())
            .tokenType("Bearer")
            .build();
  }

  @Override
  public LoginResponseDto getLoginToken(UserEntity user, HttpServletResponse response) {
    // 1. AccessToken 발급
    String accessToken = jwtTokenProvider.createToken(
            user.getUserId(),
            user.getUserEmail(),
            user.getRole()
    );

    // 2. RefreshToken 발급
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

    // 3. RefreshToken Redis 저장
    String key = String.valueOf(user.getUserId());
    long expirationSeconds = jwtTokenProvider.getRefreshTokenValidity() / 1000;
    saveRefreshToken(key, refreshToken, expirationSeconds);

    // 4. AccessToken 쿠키 저장
    jwtCookieUtil.addAccessTokenCookie(response, accessToken, jwtTokenProvider.getAccessTokenValidity() / 1000);

    // 5. 응답 DTO 생성
    return LoginResponseDto.builder()
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .build();
  }

}
