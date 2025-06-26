package com.boot.loiteBackend.web.auth.token.controller;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.token.dto.RefreshRequestDto;
import com.boot.loiteBackend.web.auth.token.entity.RefreshTokenEntity;
import com.boot.loiteBackend.web.auth.token.error.RefreshErrorCode;
import com.boot.loiteBackend.web.auth.token.repository.RefreshTokenRepository;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "토큰 관리", description = "Access Token 재발급 (Refresh Token 기반) API")
public class TokenController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 재발급",description = "만료된 액세스 토큰을 리프레시 토큰을 통해 재발급합니다.")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshRequestDto dto) {
        // 1. DB에서 리프레시 토큰 조회
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByRefreshToken(dto.getRefreshToken())
                .orElseThrow(() -> new CustomException(RefreshErrorCode.NOT_FOUND));

        // 2. 만료 여부 확인
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(RefreshErrorCode.EXPIRED);
        }

        // 3. userId 기반으로 유저 권한 조회
        UserEntity user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new CustomException(RefreshErrorCode.NOT_FOUND));

        // 4. 액세스 토큰 재발급
        String newAccessToken = jwtTokenProvider.createToken(user.getUserId(), user.getRole());

        // 5. 응답
        return ResponseEntity.ok(LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(dto.getRefreshToken()) // 기존 refresh token 그대로 유지
                .tokenType("Bearer")
                .build());
    }
}
