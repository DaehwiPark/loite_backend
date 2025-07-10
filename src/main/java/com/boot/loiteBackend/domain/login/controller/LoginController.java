package com.boot.loiteBackend.domain.login.controller;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.domain.login.dto.LoginRequestDto;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.login.service.LoginService;
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


     // 로그인 - 일반/소셜 모두 지원
    @Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호를 검증하고, AccessToken을 쿠키로 설정하며 RefreshToken은 Redis에 저장합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto dto,
            @RequestParam(name = "loginType", defaultValue = "EMAIL") String userLoginType,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(loginService.login(dto, response, userLoginType));
    }


     // 로그아웃
    @Operation(summary = "로그아웃", description = "사용자의 리프레시 토큰을 제거하고, AccessToken 쿠키를 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response
    ) {
        loginService.logout(userDetails, response);
        return ResponseEntity.ok().build();
    }


     // 내 정보 조회
     @GetMapping("/me")
     public ResponseEntity<UserSummaryDto> myInfo(
             @AuthenticationPrincipal CustomUserDetails user,
             @CookieValue(name = "AccessToken", required = false) String token
     ) {
         UserSummaryDto result = loginService.myInfo(user, token);
         return ResponseEntity.ok(result);
     }
}
