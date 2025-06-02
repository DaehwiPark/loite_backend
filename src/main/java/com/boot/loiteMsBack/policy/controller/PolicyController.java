package com.boot.loiteMsBack.policy.controller;

import com.boot.loiteMsBack.policy.dto.PolicyDto;
import com.boot.loiteMsBack.policy.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/policy")
@Tag(name = "개인정보처리방침 API", description = "개인정보 처리방침 등록, 수정, 삭제, 조회 기능을 제공합니다.")
public class PolicyController {

    private final PolicyService policyService;

    @Operation(
            summary = "전체 정책 목록 조회",
            description = "등록된 모든 개인정보처리방침 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<PolicyDto>> getAll() {
        return ResponseEntity.ok(policyService.getAll());
    }

    @Operation(
            summary = "단일 정책 조회",
            description = "지정한 ID의 개인정보처리방침 상세 정보를 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<PolicyDto> getById(
            @Parameter(description = "조회할 정책의 고유 ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(policyService.getById(id));
    }

    @Operation(
            summary = "개인정보처리방침 등록",
            description = "새로운 개인정보처리방침을 등록합니다."
    )
    @PostMapping
    public ResponseEntity<PolicyDto> create(
            @Parameter(description = "등록할 정책 정보") @RequestBody PolicyDto dto
    ) {
        PolicyDto created = policyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "개인정보처리방침 수정",
            description = "기존 개인정보처리방침 내용을 수정합니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<PolicyDto> update(
            @Parameter(description = "수정할 정책의 고유 ID") @PathVariable Long id,
            @Parameter(description = "수정할 정책 정보") @RequestBody PolicyDto dto
    ) {
        return ResponseEntity.ok(policyService.update(id, dto));
    }

    @Operation(
            summary = "개인정보처리방침 삭제",
            description = "지정한 ID의 개인정보처리방침을 삭제합니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 정책의 고유 ID") @PathVariable Long id
    ) {
        policyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
