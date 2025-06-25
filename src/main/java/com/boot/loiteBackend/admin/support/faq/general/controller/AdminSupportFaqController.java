package com.boot.loiteBackend.admin.support.faq.general.controller;

import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqDto;
import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqRequestDto;
import com.boot.loiteBackend.admin.support.faq.general.service.AdminSupportFaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/faq")
@Tag(name = "고객센터 자주 묻는 질문 API", description = "고객센터 자주 묻는 질문(FAQ) 관련 API")
public class AdminSupportFaqController {

    private final AdminSupportFaqService adminSupportFaqService;

    @Operation(summary = "FAQ 목록 조회 (검색 & 페이지네이션)", description = "검색 키워드와 페이지 정보를 기반으로 FAQ 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<AdminSupportFaqDto>> getPagedFaqs(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword
    ) {
        Page<AdminSupportFaqDto> result = adminSupportFaqService.getPagedFaqs(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "FAQ 단건 조회", description = "주어진 ID에 해당 하는 FAQ 항목을 반환합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<AdminSupportFaqDto> getFaqById(@PathVariable Long id) {
        AdminSupportFaqDto faq = adminSupportFaqService.getFaqById(id);
        return ResponseEntity.ok(faq); // 200 OK
    }

    @Operation(summary = "FAQ 등록", description = "새로운 FAQ 항목을 등록합니다.")
    @PostMapping
    public ResponseEntity<AdminSupportFaqDto> createFaq(@RequestBody AdminSupportFaqRequestDto request) {
        AdminSupportFaqDto created = adminSupportFaqService.createFaq(request);
        return ResponseEntity.status(201).body(created); // 201 Created
    }

    @Operation(summary = "FAQ 수정", description = "기존에 등록된 FAQ 내용을 수정합니다.")
    @PutMapping("/{id}/update")
    public ResponseEntity<AdminSupportFaqDto> updateFaq(
            @PathVariable Long id,
            @RequestBody AdminSupportFaqRequestDto request
    ) {
        AdminSupportFaqDto updated = adminSupportFaqService.updateFaq(id, request);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @Operation(summary = "FAQ 삭제", description = "FAQ 항목을 삭제(또는 삭제 처리)합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        adminSupportFaqService.deleteFaqById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
