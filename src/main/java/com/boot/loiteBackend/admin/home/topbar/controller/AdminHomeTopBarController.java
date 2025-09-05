package com.boot.loiteBackend.admin.home.topbar.controller;

import com.boot.loiteBackend.admin.home.topbar.dto.*;
import com.boot.loiteBackend.admin.home.topbar.service.AdminHomeTopBarService;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/top-bar")
@Tag(name = "탑바(Top Bar) 관리 API", description = "상단 탑바 관리")
public class AdminHomeTopBarController {

    private final AdminHomeTopBarService service;

    @Operation(summary = "등록", description = "대표(Y)로 등록하면 기존 대표는 자동으로 N 처리됩니다. 작성자는 로그인 사용자")
    @PostMapping
    public ResponseEntity<AdminHomeTopBarResponseDto> create(
            @Valid @RequestBody AdminHomeTopBarCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        return ResponseEntity.status(201).body(service.create(dto, loginUser.getUserId()));
    }

    @Operation(summary = "수정", description = "대표(Y)로 수정하면 기존 대표는 자동으로 N 처리됩니다. 수정자는 로그인 사용자")
    @PutMapping("/{id}")
    public ResponseEntity<AdminHomeTopBarResponseDto> update(
            @Parameter(description = "HOME_TOP_BAR_ID") @PathVariable Long id,
            @Valid @RequestBody AdminHomeTopBarUpdateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        dto.setId(id);
        Long loginUserId = (loginUser != null) ? loginUser.getUserId() : null;
        return ResponseEntity.ok(service.update(dto, loginUserId));
    }

    @Operation(summary = "삭제(하드)", description = "실제 삭제됩니다. 수행자는 로그인 사용자")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "HOME_TOP_BAR_ID") @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long loginUserId = (loginUser != null) ? loginUser.getUserId() : null;
        service.delete(id, loginUserId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상세", description = "단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeTopBarResponseDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(service.detail(id));
    }

    @Operation(summary = "목록(전체)", description = "전체 리스트 조회")
    @GetMapping
    public ResponseEntity<List<AdminHomeTopBarResponseDto>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }
}
