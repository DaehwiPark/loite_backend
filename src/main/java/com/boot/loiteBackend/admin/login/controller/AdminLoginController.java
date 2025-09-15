package com.boot.loiteBackend.admin.login.controller;

import com.boot.loiteBackend.admin.login.dto.AdminLoginRequestDto;
import com.boot.loiteBackend.admin.login.dto.AdminLogoutResponseDto;
import com.boot.loiteBackend.admin.login.service.AdminLoginService;
import com.boot.loiteBackend.domain.token.dto.TokenRequestDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "관리자 인증", description = "관리자 로그인/로그아웃/토큰 재발급 API")
public class AdminLoginController {

    private final AdminLoginService adminLoginService;   // 로그인
    private final TokenService tokenService;             // 토큰 재발급

    // --- 로그인 ---
    @Operation(summary = "관리자 로그인",
            description = "이메일/비밀번호로 관리자 전용 로그인을 수행합니다. AccessToken은 쿠키, RefreshToken은 본문에 반환됩니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody AdminLoginRequestDto body,
            HttpServletResponse response
    ) {
        LoginResponseDto dto = adminLoginService.login(body, response); // 여기서 AccessToken 쿠키 세팅 + RefreshToken 생성
        System.out.println("로그인 성공: user=" + body.getEmail() + ", refreshTokenLen=" + (dto.getRefreshToken() != null ? dto.getRefreshToken().length() : 0));
        return ResponseEntity.ok(dto);
    }

    // --- 로그아웃 ---
    @Operation(summary = "관리자 로그아웃",
            description = "AccessToken 쿠키 삭제 및 Redis의 RefreshToken 제거")
    @PostMapping("/logout")
    public ResponseEntity<AdminLogoutResponseDto> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        adminLoginService.logout(request, response);
        return ResponseEntity.ok(
                AdminLogoutResponseDto.builder()
                        .success(true)
                        .message("로그아웃이 완료되었습니다.")
                        .build()
        );
        // 필요시: return ResponseEntity.noContent().build();
    }

    // --- 토큰 재발급 ---
    @Operation(summary = "AccessToken 재발급",
            description = "RefreshToken을 검증하고 새로운 AccessToken을 쿠키로 발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @RequestBody TokenRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(tokenService.refresh(dto, response));
    }
}
