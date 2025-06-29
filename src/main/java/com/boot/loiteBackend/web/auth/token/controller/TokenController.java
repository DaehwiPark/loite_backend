package com.boot.loiteBackend.web.auth.token.controller;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.token.dto.RefreshRequestDto;
import com.boot.loiteBackend.web.auth.token.error.RefreshErrorCode;
import com.boot.loiteBackend.web.auth.token.service.RefreshTokenService;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "토큰 관리", description = "Access Token 재발급 (Refresh Token 기반) API")
public class TokenController {

  private final RefreshTokenService refreshTokenService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponseDto> refresh(
    @RequestBody RefreshRequestDto dto,
    HttpServletResponse response
  ) {
    // 1. RefreshToken에서 userId 추출
    Long userId = jwtTokenProvider.getUserId(dto.getRefreshToken());
    String key = String.valueOf(userId); // Redis 키는 String

    // 2. Redis에서 저장된 RefreshToken 가져오기
    String storedToken = refreshTokenService.getRefreshToken(key)
      .orElseThrow(() -> new CustomException(RefreshErrorCode.NOT_FOUND));

    // 3. 토큰 값 검증
    if (!storedToken.equals(dto.getRefreshToken())) {
      throw new CustomException(RefreshErrorCode.MISMATCH);
    }

    // 4. 사용자 정보 조회
    UserEntity user = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(RefreshErrorCode.NOT_FOUND));

    // 5. 새로운 AccessToken 발급
    String newAccessToken = jwtTokenProvider.createToken(
      user.getUserId(),
      user.getUserEmail(),
      user.getRole()
    );

    // 6. refreshToken TTL 연장
    refreshTokenService.extendRefreshTokenTTL(String.valueOf(user.getUserId()), dto.getRefreshToken());

    // 7. AccessToken을 쿠키로 설정
    Cookie accessTokenCookie = new Cookie("AccessToken", newAccessToken);
    accessTokenCookie.setHttpOnly(true);
    accessTokenCookie.setPath("/");
    accessTokenCookie.setMaxAge((int) jwtTokenProvider.getAccessTokenValidity() / 1000);
    accessTokenCookie.setSecure(true); // 프로덕션 환경 기준
    accessTokenCookie.setAttribute("SameSite", "Strict");

    response.addCookie(accessTokenCookie);

    // 7. 응답 반환
    return ResponseEntity.ok(LoginResponseDto.builder()
      .refreshToken(dto.getRefreshToken())
      .tokenType("Bearer")
      .build());
  }
}