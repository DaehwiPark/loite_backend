package com.boot.loiteBackend.domain.login.controller;

import com.boot.loiteBackend.domain.login.dto.VerifyRequestDto;
import com.boot.loiteBackend.global.response.ApiResponse;
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

    @Operation(
            summary = "로그인",
            description = "사용자의 이메일과 비밀번호(또는 소셜 로그인 정보)를 검증한 후, AccessToken은 응답 쿠키에 설정되고 RefreshToken은 Redis에 저장됩니다. " +
                    "loginType 파라미터로 EMAIL, GOOGLE, KAKAO, NAVER 등의 로그인 유형을 구분할 수 있습니다."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto dto,
            @RequestParam(name = "loginType", defaultValue = "EMAIL") String userLoginType,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(loginService.login(dto, response, userLoginType));
    }


    @Operation(
            summary = "로그아웃",
            description = "현재 로그인된 사용자의 RefreshToken을 Redis에서 제거하고, 브라우저에 저장된 AccessToken 쿠키를 만료시켜 로그아웃 처리합니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response
    ) {
        loginService.logout(userDetails, response);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "현재 로그인된 사용자 정보 조회",
            description = "AccessToken을 통해 인증된 사용자의 정보를 반환합니다. 반환되는 정보에는 이름, 이메일, 소셜 연동 여부, 권한 등이 포함됩니다. " +
                    "AccessToken은 쿠키로 전달되며, 유효하지 않을 경우 401 응답을 받을 수 있습니다."
    )
    @GetMapping("/me")
    public ResponseEntity<UserSummaryDto> myInfo(
            @AuthenticationPrincipal CustomUserDetails user,
            @CookieValue(name = "AccessToken", required = false) String token
    ) {
        UserSummaryDto result = loginService.myInfo(user, token);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "비밀번호 인증 (본인 확인)",
            description = "마이페이지 접근 등 민감한 작업 전에 사용자 비밀번호를 다시 입력받아 검증합니다."
    )
    @PostMapping("/verify-password")
    public ResponseEntity<ApiResponse<Boolean>> verifyPassword(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody VerifyRequestDto dto
    ) {
        boolean result = loginService.check(user, dto.getPassword());
        return ResponseEntity.ok(ApiResponse.ok(result, "비밀번호 인증 성공"));
    }

}
