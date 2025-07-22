package com.boot.loiteBackend.web.support.counsel.controller;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.support.counsel.dto.SupportCounselPasswordVerifyDto;
import com.boot.loiteBackend.web.support.counsel.dto.SupportCounselRequestDto;
import com.boot.loiteBackend.web.support.counsel.dto.SupportCounselResponseDto;
import com.boot.loiteBackend.web.support.counsel.service.SupportCounselService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/support/counsel")
@RequiredArgsConstructor
@Tag(name = "1:1 문의 API", description = "1:1 문의 등록, 조회, 비밀번호 확인 등 사용자 기능")
public class SupportCounselController {

    private final SupportCounselService supportCounselService;

    @Operation(summary = "1:1 문의 등록", description = "로그인한 사용자가 1:1 문의를 작성합니다.")
    @PostMapping
    public ResponseEntity<Long> createCounsel(
            @RequestBody SupportCounselRequestDto dto,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user) {
        Long counselId = supportCounselService.createCounsel(user.getUserId(), dto);
        return ResponseEntity.ok(counselId);
    }

    @Operation(summary = "내 문의 목록 조회", description = "로그인한 사용자의 모든 1:1 문의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SupportCounselResponseDto>> getMyCounsels(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(supportCounselService.getMyCounsels(user.getUserId()));
    }

    @Operation(summary = "문의 상세 조회", description = "문의 ID로 상세 정보를 조회합니다. 비밀글의 경우 비밀번호 확인이 필요할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportCounselResponseDto> getDetail(
            @Parameter(description = "문의 ID", example = "15") @PathVariable Long id,
            @Parameter(description = "비밀글인 경우 입력하는 비밀번호", example = "mypassword123!") @RequestParam(required = false) String password,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(supportCounselService.getCounselDetail(id, user.getUserId(), password));
    }

    @Operation(summary = "비밀글 비밀번호 확인", description = "비밀글 접근 시 입력한 비밀번호가 맞는지 확인합니다.")
    @PostMapping("/{id}/verify-password")
    public ResponseEntity<Boolean> verifyPassword(
            @Parameter(description = "문의 ID", example = "15")
            @PathVariable Long id,
            @RequestBody SupportCounselPasswordVerifyDto dto) {
        return ResponseEntity.ok(supportCounselService.verifyPassword(id, dto.getInputPassword()));
    }
}