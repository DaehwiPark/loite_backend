package com.boot.loiteBackend.web.user.general.controller;

import com.boot.loiteBackend.domain.login.dto.VerifyRequestDto;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.user.general.dto.*;
import com.boot.loiteBackend.web.user.general.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/users")
@Tag(name = "회원 관리 API", description = "회원 계정 관련 API (비회원 접근 불가)")
public class PrivateUserController {

    private final UserService userService;

    @Operation(summary = "회원 정보 조회", description = "현재 로그인된 사용자의 회원 기본 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        UserInfoDto userInfo = userService.getUserInfo(loginUser);
        return ResponseEntity.ok(ApiResponse.ok(userInfo, "회원 정보가 조회 완료 되었습니다."));
    }

    @Operation(summary = "회원 기본 정보 수정", description = "휴대폰 번호와 생년월일을 수정합니다. (휴대폰 번호는 중복 확인 포함)")
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateUserInfo(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestBody UserUpdateInfoRequestDto request
    ) {
        userService.updateUserInfo(loginUser, request);
        return ResponseEntity.ok(ApiResponse.ok("회원 정보가 수정되었습니다."));
    }

    @Operation(summary = "마케팅 수신 동의 수정", description = "이메일 및 SMS 마케팅 수신 동의 여부를 수정합니다.")
    @PutMapping("/agreement")
    public ResponseEntity<ApiResponse<Void>> updateMarketingAgreement(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestBody UserMarketingAgreementRequestDto request
    ) {
        userService.updateMarketingAgreement(loginUser, request);
        return ResponseEntity.ok(ApiResponse.ok("마케팅 수신 동의 정보가 수정되었습니다."));
    }

    // TODO: 현재 Hard delete 로 구현되어있으나 추후 soft delete 로 변경 필요
    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자의 회원 정보를 삭제(탈퇴)합니다.")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestParam(required = false) String token,
            HttpServletResponse response
    ) {
        userService.withdraw(loginUser, token, response);
        return ResponseEntity.ok(ApiResponse.ok("회원 탈퇴가 완료되었습니다."));
    }

    @Operation(summary = "비밀번호 인증 (본인 확인)", description = "마이페이지 접근 등 민감한 작업 전에 사용자 비밀번호를 다시 입력받아 검증합니다.")
    @PostMapping("/verify-password")
    public ResponseEntity<ApiResponse<Boolean>> verifyPassword(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody VerifyRequestDto dto
    ) {
        boolean result = userService.check(user, dto.getPassword());
        return ResponseEntity.ok(ApiResponse.ok(result, "비밀번호 인증 성공"));
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호를 확인하고, 새 비밀번호로 변경합니다.")
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestBody @Valid UpdatePasswordRequestDto request
    ) {
        userService.changePassword(loginUser, request);
        return ResponseEntity.ok(ApiResponse.ok("비밀번호가 성공적으로 변경되었습니다."));
    }

    // TODO: 관리자 API로 이동 예정
    @Operation(summary = "특정 회원 탈퇴", description = "userId를 입력 받아 해당 사용자의 계정을 삭제합니다. (관리자 전용)")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> withdrawById(@PathVariable Long userId) {
        userService.withdrawById(userId);
        return ResponseEntity.ok(ApiResponse.ok("회원 탈퇴가 완료되었습니다."));
    }

}