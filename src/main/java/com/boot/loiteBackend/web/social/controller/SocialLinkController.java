package com.boot.loiteBackend.web.social.controller;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.dto.SocialLinkingStatusResponseDto;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.web.social.service.SocialLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/link")
@RequiredArgsConstructor
@Tag(name = "소셜 계정 연동", description = "일반 계정과 소셜 계정을 연동하는 API")
public class SocialLinkController {

    private final OAuthHandlerResolver handlerResolver;
    private final SocialLinkService socialLinkService;

    @GetMapping("/status")
    @Operation(summary = "소셜 연동 현황 조회", description = "현재 로그인된 사용자의 소셜 연동 상태를 확인합니다.")
    public ResponseEntity<ApiResponse<SocialLinkingStatusResponseDto>> getSocialLinkingStatus(
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        SocialLinkingStatusResponseDto status = socialLinkService.getSocialLinkingStatus(loginUser);
        return ResponseEntity.ok(ApiResponse.ok(status));
    }

    @GetMapping("/{provider}")
    @Operation(summary = "소셜 연동 URL 발급", description = "카카오/구글/네이버 등의 연동 URL을 생성합니다.")
    public ResponseEntity<ApiResponse<String>> getLoginUrl(@PathVariable String provider) {
        String url = handlerResolver.resolveLink(provider).getLinkUrl();
        return ResponseEntity.ok(ApiResponse.ok(url));
    }

    @GetMapping("/{provider}/callback")
    @Operation(summary = "소셜 연동 콜백", description = "소셜 연동 후 callback URL로 접근되며, 연동 결과를 처리합니다.")
    public ResponseEntity<ApiResponse<LoginResponseDto>> linkCallback(
            @PathVariable String provider,
            @RequestParam String code,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            HttpServletResponse response
    ) {
        String userLoginType = provider.toUpperCase();
        ApiResponse<LoginResponseDto> result = socialLinkService.link(provider, code, loginUser, response, userLoginType);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{provider}/unlink")
    @Operation(summary = "소셜 연동 해제", description = "선택한 소셜 계정의 연동을 해제합니다.")
    public ResponseEntity<ApiResponse<String>> unlinkAccount(
            @PathVariable String provider,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        ApiResponse<String> result = socialLinkService.unlinkAccount(provider, loginUser);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{provider}/verify")
    @Operation(summary = "소셜 인증 URL 발급", description = "카카오/구글/네이버 등의 인증 URL을 생성합니다.")
    public ResponseEntity<ApiResponse<String>> getVerifyUrl(@PathVariable String provider) {
        String url = handlerResolver.resolveVerify(provider).getVerifyUrl();
        return ResponseEntity.ok(ApiResponse.ok(url));
    }

    @GetMapping("/{provider}/verify/callback")
    @Operation(summary = "소셜 인증 콜백", description = "소셜 인증 후 callback URL로 접근되며, 현재 로그인 사용자 정보와 비교하여 일치 여부를 확인합니다.")
    public ResponseEntity<ApiResponse<Boolean>> verifyCallback(
            @PathVariable String provider,
            @RequestParam String code,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        boolean result = socialLinkService.verifySocialAuthentication(provider, code, loginUser);
        return ResponseEntity.ok(ApiResponse.ok(result, "소셜 인증 비교 결과 반환"));
    }

}