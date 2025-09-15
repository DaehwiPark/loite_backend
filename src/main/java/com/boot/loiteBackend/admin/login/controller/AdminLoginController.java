package com.boot.loiteBackend.admin.login.controller;

import com.boot.loiteBackend.admin.login.dto.AdminLoginRequestDto;
import com.boot.loiteBackend.admin.login.dto.AdminLogoutResponseDto;
import com.boot.loiteBackend.admin.login.service.AdminLoginService;
import com.boot.loiteBackend.admin.user.dto.AdminUserSummaryDto;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.domain.token.dto.TokenRequestDto;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.user.general.dto.UserSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletResponse response
    ) {
        adminLoginService.logout(user, response);
        return ResponseEntity.ok(
                AdminLogoutResponseDto.builder()
                        .success(true)
                        .message("로그아웃이 완료되었습니다.")
                        .build()
        );
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

    @Operation(
            summary = "현재 로그인된 사용자 정보 조회",
            description = "AccessToken을 통해 인증된 사용자의 정보를 반환합니다. 반환되는 정보에는 이름, 이메일, 소셜 연동 여부, 권한 등이 포함됩니다. " +
                    "AccessToken은 쿠키로 전달되며, 유효하지 않을 경우 401 응답을 받을 수 있습니다."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminUserSummaryDto>> myInfo(
            @AuthenticationPrincipal CustomUserDetails user,
            @CookieValue(name = "AccessToken", required = false) String token
    ) {
        AdminUserSummaryDto result = adminLoginService.myInfo(user, token);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
