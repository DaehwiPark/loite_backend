package com.boot.loiteBackend.domain.token.controller;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.token.dto.TokenRequestDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "토큰 관리", description = "Access Token 재발급 (Refresh Token 기반) API")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "AccessToken 재발급", description = "RefreshToken을 검증하고 새로운 AccessToken을 쿠키로 발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @RequestBody TokenRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(tokenService.refresh(dto, response));
    }
}
