package com.boot.loiteBackend.web.auth.login.controller;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.auth.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.login.service.LoginService;
import com.boot.loiteBackend.web.user.dto.UserSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "로그인/로그아웃 관련 API", description = "로그인, 로그아웃, 사용자 정보 조회 등 인증 관련 API")
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호를 검증하고, AccessToken을 쿠키로 설정하며 RefreshToken은 Redis에 저장합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(loginService.login(dto, response));
    }

    @Operation(summary = "로그아웃", description = "사용자의 리프레시 토큰을 제거하고, AccessToken 쿠키를 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response
    ) {
        loginService.logout(userDetails, response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자의 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<UserSummaryDto> myInfo(@AuthenticationPrincipal CustomUserDetails user) {
        UserSummaryDto result = loginService.myInfo(user);
        return ResponseEntity.ok(result);
    }
}
