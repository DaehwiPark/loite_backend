package com.boot.loiteBackend.web.auth.token.controller;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.token.dto.RefreshRequestDto;
import com.boot.loiteBackend.web.auth.token.error.RefreshErrorCode;
import com.boot.loiteBackend.web.auth.token.redis.RefreshToken;
import com.boot.loiteBackend.web.auth.token.repository.RefreshTokenRepository;
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

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @RequestBody RefreshRequestDto dto,
            HttpServletResponse response
    ) {
        // 1. RefreshToken 파싱하여 username 추출
        String username = jwtTokenProvider.getUsername(dto.getRefreshToken());

        // 2. Redis에서 토큰 조회
        RefreshToken storedToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new CustomException(RefreshErrorCode.NOT_FOUND));

        // 3. 토큰 값 검증
        if (!storedToken.getToken().equals(dto.getRefreshToken())) {
            throw new CustomException(RefreshErrorCode.MISMATCH);
        }

        // 4. 사용자 정보 조회
        UserEntity user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new CustomException(RefreshErrorCode.NOT_FOUND));

        // 5. 새로운 AccessToken 발급
        String newAccessToken = jwtTokenProvider.createToken(
                user.getUserId(),
                user.getUserEmail(),
                user.getRole()
        );

        // 6. AccessToken을 쿠키로 설정
        Cookie accessTokenCookie = new Cookie("AccessToken", newAccessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) jwtTokenProvider.getAccessTokenValidity() / 1000);
        accessTokenCookie.setSecure(true); // prod 환경일 경우 true
        accessTokenCookie.setAttribute("SameSite", "Strict");

        response.addCookie(accessTokenCookie);

        // 7. 응답
        return ResponseEntity.ok(LoginResponseDto.builder()
                .refreshToken(dto.getRefreshToken())
                .tokenType("Bearer")
                .build());
    }
}
