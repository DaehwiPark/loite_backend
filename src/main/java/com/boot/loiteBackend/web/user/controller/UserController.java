package com.boot.loiteBackend.web.user.controller;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.user.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/user")
@Tag(name = "회원가입", description = "회원 계정 생성 관련 API (비회원 접근 가능)")
public class UserController {

    private final UserService userService;

    @Operation( summary = "회원가입", description = "사용자의 이메일, 비밀번호, 이름, 연락처 등의 정보를 입력받아 신규 회원 계정을 생성합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signup(@RequestBody UserCreateRequestDto dto) {
        Long userId = userService.signup(dto);
        return ResponseEntity.ok(
                ApiResponse.<Long>builder()
                        .success(true)
                        .message("회원가입이 완료되었습니다.")
                        .data(userId)
                        .build()
        );
    }
    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자의 회원 정보를 삭제(탈퇴)합니다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw() {
        userService.withdraw(); // 현재 로그인한 사용자 기준 탈퇴 처리
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("회원 탈퇴가 완료되었습니다.")
                        .build()
        );
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
