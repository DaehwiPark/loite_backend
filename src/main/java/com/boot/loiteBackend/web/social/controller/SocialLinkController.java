package com.boot.loiteBackend.web.social.controller;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.web.social.service.SocialLinkService;
import com.boot.loiteBackend.web.user.entity.UserEntity;
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

    @GetMapping("/{provider}")
    @Operation(summary = "소셜 연동 URL 발급", description = "카카오/구글/네이버 등의 연동용 인증 URL을 생성합니다.")
    public ResponseEntity<ApiResponse<String>> getLoginUrl(@PathVariable String provider) {
        String url = handlerResolver.resolveLink(provider).getUrl();
        return ResponseEntity.ok(ApiResponse.ok(url));
    }

    @GetMapping("/{provider}/callback")
    @Operation(summary = "소셜 연동 콜백", description = "소셜 연동 후 callback URL로 접근되며, 연동 결과를 처리합니다.")
    public ResponseEntity<ApiResponse<LoginResponseDto>> linkCallback(
            @PathVariable String provider,
            @RequestParam String code,
            @AuthenticationPrincipal UserEntity loginUser,
            HttpServletResponse response
    ) {
        ApiResponse<LoginResponseDto> result = socialLinkService.link(provider, code, loginUser, response);
        return ResponseEntity.ok(result);
    }


//    @PostMapping("/{provider}/unlink")
//    public ResponseEntity<String> unlinkAccount(@PathVariable String provider, @RequestBody LoginVO loginVO) {
//        boolean success = socialLinkService.unlinkAccount(provider, loginVO);
//        return success ? ResponseEntity.ok("true") : ResponseEntity.badRequest().body("연결 해제 실패");
//    }
//
//    @GetMapping("/{provider}/check")
//    public ResponseEntity<String> redirectToCheckPage(@PathVariable String provider) {
//        return ResponseEntity.ok(socialLinkService.buildSocialCheckUrl(provider));
//    }
//
//    @GetMapping("/{provider}/check/callback")
//    public void socialCheckCallback(
//            @PathVariable String provider,
//            @RequestParam String code,
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        PrintWriter out = response.getWriter();
//        var result = socialLinkService.checkSocial(provider, code, request);
//
//        out.println("<script>");
//        out.println("window.close();");
//        out.println("window.opener.postMessage(" + result + ", '*');");
//        out.println("</script>");
//        out.flush();
//    }
}
