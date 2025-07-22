package com.boot.loiteBackend.web.user.general.controller;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.user.general.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.general.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/user")
@Tag(name = "회원가입 API", description = "회원 계정 생성 관련 API (비회원 접근 가능)")
public class PublicUserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "사용자의 이메일, 비밀번호, 이름, 연락처 등의 정보를 입력받아 신규 회원 계정을 생성합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signup(@Valid @RequestBody UserCreateRequestDto dto) {
        Long userId = userService.signup(dto);
        return ResponseEntity.ok(ApiResponse.ok(userId, "회원가입이 완료되었습니다."));
    }

    @Operation(summary = "이메일 중복 확인", description = "입력한 이메일이 이미 가입된 이메일인지 확인합니다.")
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean duplicated = userService.isEmailDuplicated(email);
        String message = duplicated ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.";
        return ResponseEntity.ok(ApiResponse.ok(duplicated, message));
    }
}