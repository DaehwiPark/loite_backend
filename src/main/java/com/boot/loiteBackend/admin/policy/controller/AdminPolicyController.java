package com.boot.loiteBackend.admin.policy.controller;

import com.boot.loiteBackend.admin.policy.dto.AdminPolicyDto;
import com.boot.loiteBackend.admin.policy.service.AdminPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/policy")
@Tag(name = "개인정보처리방침 API", description = "개인정보 처리방침 등록, 수정, 삭제, 조회 기능을 제공합니다.")
public class AdminPolicyController {

    private final AdminPolicyService adminPolicyService;

    @Operation(summary = "정책 목록 조회 (검색 및 페이징)", description = "키워드로 검색하고, 페이지네이션 처리된 개인정보처리방침 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<AdminPolicyDto>> getPagedPolicy(
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<AdminPolicyDto> result = adminPolicyService.getPagedPolicy(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "단일 정책 조회",description = "지정한 ID의 개인정보처리방침 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<AdminPolicyDto> getById(
            @Parameter(description = "조회할 정책의 고유 ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(adminPolicyService.getById(id));
    }

    @Operation(summary = "개인정보처리방침 등록",description = "새로운 개인정보처리방침을 등록합니다.")
    @PostMapping
    public ResponseEntity<AdminPolicyDto> create(
            @Parameter(description = "등록할 정책 정보") @RequestBody AdminPolicyDto dto
    ) {
        AdminPolicyDto created = adminPolicyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "개인정보처리방침 수정",description = "기존 개인정보처리방침 내용을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<AdminPolicyDto> update(
            @Parameter(description = "수정할 정책의 고유 ID") @PathVariable Long id,
            @Parameter(description = "수정할 정책 정보") @RequestBody AdminPolicyDto dto
    ) {
        return ResponseEntity.ok(adminPolicyService.update(id, dto));
    }

    @Operation(summary = "개인정보처리방침 삭제",description = "지정한 ID의 개인정보처리방침을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 정책의 고유 ID") @PathVariable Long id
    ) {
        adminPolicyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
