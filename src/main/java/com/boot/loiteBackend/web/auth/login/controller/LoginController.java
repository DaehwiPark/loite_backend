package com.boot.loiteBackend.web.auth.login.controller;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.auth.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.login.service.LoginService;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.auth.token.repository.RefreshTokenRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "로그인, 토큰 발급 등 인증 관련 API")
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호를 검증하고, AccessToken과 RefreshToken을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(loginService.login(dto));
    }

    @Operation(summary = "로그아웃", description = "사용자의 리프레시 토큰을 제거하여 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        refreshTokenRepository.deleteById(userDetails.getUserId());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<String> myInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok("로그인한 유저 ID: " + user.getUserId() + ", Role: " + user.getRole());
    }
}
