package com.boot.loiteBackend.web.user.general.controller;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.service.SocialLinkService;
import com.boot.loiteBackend.web.user.general.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/user")
@Tag(name = "회원탈퇴 API", description = "회원 계정 탈퇴 관련 API (비회원 접근 불가)")
public class PrivateUserController {

    private final UserService userService;
    private final SocialLinkService socialLinkService;

    @DeleteMapping("/withdrawal")
    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자의 회원 정보를 삭제(탈퇴)합니다.")
    public ResponseEntity<ApiResponse<String>> withdraw(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestParam(required = false) String token
    ) {
        ApiResponse<String> result = userService.withdraw(loginUser, token);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "특정 회원 탈퇴", description = "userId를 입력 받아 해당 사용자의 계정을 삭제합니다. (관리자 전용)")
    @DeleteMapping("/withdraw/{userId}")
    public ResponseEntity<ApiResponse<Void>> withdrawById(@PathVariable Long userId) {
        userService.withdrawById(userId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("회원 탈퇴가 완료되었습니다.")
                        .build()
        );
    }
}
