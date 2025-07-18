package com.boot.loiteBackend.web.social.controller;

import com.boot.loiteBackend.web.social.dto.SocialUserRegistrationDto;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.web.social.service.SocialLoginService;
import com.boot.loiteBackend.web.social.service.SocialRegisterService;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth/login")
@Tag(name = "소셜 로그인", description = "카카오, 네이버, 구글 등의 OAuth2 기반 로그인 및 신규 사용자 등록 처리 API")
@RequiredArgsConstructor
public class SocialLoginController {

    private final OAuthHandlerResolver handlerResolver;
    private final SocialLoginService socialLoginService;
    private final SocialRegisterService socialRegisterService;

    @GetMapping("/{provider}")
    @Operation(
            summary = "소셜 로그인 URL 생성",
            description = "요청한 provider(KAKAO, NAVER, GOOGLE 등)의 인증 페이지로 이동하기 위한 리디렉션 URL을 생성합니다.\n\n" +
                    "- `provider`: 로그인 플랫폼 이름 (예: kakao, naver, google)"
    )
    public ResponseEntity<ApiResponse<String>> getLoginUrl(@PathVariable String provider) {
        String url = handlerResolver.resolveLogin(provider).getUrl();
        return ResponseEntity.ok(ApiResponse.ok(url));
    }

    @GetMapping("/{provider}/callback")
    @Operation(
            summary = "소셜 로그인 콜백 처리",
            description = "OAuth 인증 후 인가 코드(code)를 받아 access token을 요청하고 로그인 처리를 수행합니다.\n" +
                    "- 기존 회원: JWT 토큰 반환\n" +
                    "- 신규 사용자: 회원가입 유도 응답 반환\n" +
                    "- `provider`: 로그인 플랫폼 이름\n" +
                    "- `code`: 소셜 플랫폼에서 발급한 인가 코드"
    )
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginCallback(
            @PathVariable String provider,
            @RequestParam String code,
            HttpServletResponse response
    ) {
        ApiResponse<LoginResponseDto> result = socialLoginService.login(provider, code, response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{provider}/register")
    @Operation(
            summary = "소셜 사용자 등록",
            description = "소셜 로그인으로 최초 접속한 사용자의 정보를 기반으로 회원가입을 처리합니다.\n" +
                    "- `provider`: 로그인 플랫폼 이름\n" +
                    "- `registrationDto`: 사용자 이름, 이메일, 약관 동의 등 회원가입에 필요한 정보"
    )
    public ResponseEntity<ApiResponse<LoginResponseDto>> registerSocialUser(
            @PathVariable String provider,
            @Valid @RequestBody SocialUserRegistrationDto registrationDto,
            HttpServletResponse response
    ) {
        // 로그인 타입은 provider 값 그대로 사용
        String userLoginType = provider.toUpperCase();

        // 서비스 호출 시 로그인 방식 전달
        LoginResponseDto loginResult = socialRegisterService.register(
                provider,
                registrationDto,
                response,
                userLoginType
        );

        return ResponseEntity.ok(
                ApiResponse.ok(loginResult, userLoginType + " 사용자 등록 및 로그인 성공")
        );
    }
}
