// 📁 com.boot.loiteBackend.web.auth.login.controller.LoginController

package com.boot.loiteBackend.web.auth.login.controller;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.auth.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.login.service.LoginService;
import com.boot.loiteBackend.web.auth.token.service.RefreshTokenService;
import com.boot.loiteBackend.web.user.dto.UserSummaryDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    private boolean isDev() {
        return "dev".equals(activeProfile);
    }

    @Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호를 검증하고, AccessToken을 쿠키로, RefreshToken은 Redis에 저장합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response
    ) {
        // 1. 로그인 처리
        LoginResponseDto result = loginService.login(dto);
        String accessToken = result.getAccessToken();
        String refreshToken = result.getRefreshToken();
        String username = jwtTokenProvider.getUsername(accessToken);

        // 2. AccessToken → 쿠키로 전송
        Cookie accessTokenCookie = new Cookie("AccessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) jwtTokenProvider.getAccessTokenValidity() / 1000);
        accessTokenCookie.setSecure(!isDev());
        accessTokenCookie.setAttribute("SameSite", "Strict");
        response.addCookie(accessTokenCookie);

        // 3. RefreshToken → Redis 저장
        long refreshTtlSeconds = jwtTokenProvider.getRefreshTokenValidity() / 1000;
        refreshTokenService.saveRefreshToken(username, refreshToken, refreshTtlSeconds);

        // 4. 응답 구성
        return ResponseEntity.ok(LoginResponseDto.builder()
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build());
    }

    @Operation(summary = "로그아웃", description = "사용자의 리프레시 토큰을 제거하고, AccessToken 쿠키를 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       HttpServletResponse response) {
        refreshTokenService.deleteRefreshToken(userDetails.getUsername()); // Redis 기준은 username

        Cookie deleteCookie = new Cookie("AccessToken", null);
        deleteCookie.setHttpOnly(true);
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0);
        deleteCookie.setSecure(!isDev());
        deleteCookie.setAttribute("SameSite", "Strict");

        response.addCookie(deleteCookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인된 사용자의 정보를 반환합니다.")
    public ResponseEntity<UserSummaryDto> myInfo(@AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(UserSummaryDto.builder()
                .userId(user.getUserId())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
                .role(user.getRole())
                .build());
    }
}
