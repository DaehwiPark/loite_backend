package com.boot.loiteBackend.web.social.login.controller;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.social.login.dto.SocialUserRegistrationDto;
import com.boot.loiteBackend.web.social.login.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/login/kakao")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "카카오 소셜 로그인", description = "카카오 로그인 및 회원가입 API")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping
    @Operation(summary = "카카오 로그인 리디렉션 URL", description = "카카오 OAuth 인증을 위한 리디렉션 URL을 반환합니다.")
    public ResponseEntity<ApiResponse<String>> getKakaoLoginUrl() {
        String kakaoLoginUrl = kakaoLoginService.getKakaoLoginUrl();
        return ResponseEntity.ok(ApiResponse.ok(kakaoLoginUrl, "카카오 로그인 URL 생성 성공"));
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 인가 코드(code)를 받아 액세스 토큰을 발급받고, 기존 회원이면 로그인 처리, 신규 사용자는 회원가입 유도 응답을 반환합니다.")
    public ResponseEntity<ApiResponse<LoginResponseDto>> kakaoLoginCallback(
            @Parameter(description = "카카오에서 전달받은 인가 코드", required = true)
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        ApiResponse<LoginResponseDto> result = kakaoLoginService.kakaoLoginCallback(code, response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    @Operation(summary = "카카오 신규 사용자 등록", description = "카카오로 처음 로그인하는 사용자의 회원가입을 처리합니다.")
    public ResponseEntity<ApiResponse<LoginResponseDto>> registerKakaoUser(
            @Parameter(description = "사용자 등록 정보", required = true)
            @Valid @RequestBody SocialUserRegistrationDto registrationDto,
            HttpServletResponse response
    ) {
        LoginResponseDto loginResult = kakaoLoginService.registerKakaoUser(registrationDto, response);
        return ResponseEntity.ok(ApiResponse.ok(loginResult, "카카오 사용자 등록 및 로그인 성공"));
    }

    @GetMapping("/user-info")
    @Operation(summary = "카카오 사용자 정보 조회", description = "카카오 액세스 토큰으로 사용자 정보를 조회합니다. (개발/테스트 용도)")
    public ResponseEntity<ApiResponse<Object>> getKakaoUserInfo(
            @Parameter(description = "카카오 액세스 토큰", required = true)
            @RequestParam("accessToken") String accessToken
    ) {
        Object userInfo = kakaoLoginService.getKakaoUserInfo(accessToken);
        return ResponseEntity.ok(ApiResponse.ok(userInfo, "카카오 사용자 정보 조회 성공"));
    }
}
