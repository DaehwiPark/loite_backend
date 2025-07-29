package com.boot.loiteBackend.web.user.general.controller;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.user.general.dto.*;
import com.boot.loiteBackend.web.user.general.entity.UserEntity;
import com.boot.loiteBackend.web.user.general.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/users")
@Tag(name = "회원가입 및 인증 API", description = "회원가입, 이메일 중복 확인, 아이디/비밀번호 찾기 등 비회원 접근이 가능한 API")
public class PublicUserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 이름, 연락처 등의 정보를 입력받아 신규 회원 계정을 생성합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> createUser(@Valid @RequestBody UserCreateRequestDto dto) {
        Long userId = userService.signup(dto);
        return ResponseEntity.ok(ApiResponse.ok(userId, "회원가입이 완료되었습니다."));
    }

    @Operation(summary = "이메일 중복 확인", description = "회원가입 시 입력한 이메일이 이미 사용 중인지 확인합니다.")
    @GetMapping("/email/check")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean duplicated = userService.isEmailDuplicated(email);
        String message = duplicated ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.";
        return ResponseEntity.ok(ApiResponse.ok(duplicated, message));
    }

    @Operation(summary = "핸드폰 번호 중복 확인", description = "회원가입 시 입력한 핸드폰 번호가 이미 사용 중인지 확인합니다.")
    @GetMapping("/phone/check")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@RequestParam String phone) {
        boolean duplicated = userService.isPhoneDuplicated(phone);
        String message = duplicated ? "이미 사용 중인 핸드폰 번호입니다." : "사용 가능한 핸드폰 번호입니다.";
        return ResponseEntity.ok(ApiResponse.ok(duplicated, message));
    }

    @Operation(summary = "아이디(이메일) 찾기", description = "회원가입 시 등록한 이름과 휴대폰 번호를 입력받아 해당 이메일과 가입일자를 반환합니다.")
    @PostMapping("/emails/search")
    public ResponseEntity<ApiResponse<String>> searchUserEmail(@Valid @RequestBody FindUserIdRequestDto dto) {
        UserEntity user = userService.findUserEntity(dto);
        String email = user.getUserEmail();
        LocalDateTime createdAt = user.getCreatedAt();
        Map<String, Object> extra = new HashMap<>();
        extra.put("createdAt", createdAt);
        return ResponseEntity.ok(ApiResponse.ok(email, "가입된 이메일입니다.", extra));
    }

    @Operation(summary = "비밀번호 재설정 본인 확인",
            description = "이름, 이메일, 휴대폰 번호를 기반으로 등록된 회원인지 확인합니다.")
    @PostMapping("/password/search")
    public ResponseEntity<ApiResponse<String>> verifyUserForPasswordReset(@Valid @RequestBody ResetPasswordRequestDto dto) {
        userService.validateUserForPasswordReset(dto);
        return ResponseEntity.ok(ApiResponse.ok(null, "회원 정보가 확인되었습니다. 새 비밀번호를 입력해주세요."));
    }

    @Operation(summary = "비밀번호 재설정", description = "본인 확인 이후, 새로운 비밀번호를 설정합니다.")
    @PutMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody UpdatePasswordRequestDto dto) {
        userService.updatePassword(dto);
        return ResponseEntity.ok(ApiResponse.ok(null, "비밀번호가 성공적으로 변경되었습니다."));
    }
}