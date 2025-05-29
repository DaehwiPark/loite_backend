package com.boot.loiteMsBack.support.faq.category.controller;

import com.boot.loiteMsBack.support.faq.category.dto.SupportFaqCategoryDto;
import com.boot.loiteMsBack.support.faq.category.dto.SupportFaqCategoryRequestDto;
import com.boot.loiteMsBack.support.faq.category.service.SupportFaqCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/faqs/category")
@Tag(name = "Support FAQ Category", description = "고객센터 자주 묻는 질문(FAQ) 카테고리 관련 API")
public class SupportFaqCategoryController {

    private final SupportFaqCategoryService supportFaqCategoryService;

    @Operation(summary = "FAQ 카테고리 생성", description = "새로운 FAQ 카테고리 항목을 생성합니다.")
    @PostMapping
    public ResponseEntity<SupportFaqCategoryDto> createCategory(@RequestBody SupportFaqCategoryRequestDto request) {
        SupportFaqCategoryDto created = supportFaqCategoryService.createCategory(request);
        return ResponseEntity.status(201).body(created); // 201 Created
    }

    @Operation(summary = "FAQ 카테고리 전체 조회", description = "등록된 모든 FAQ 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SupportFaqCategoryDto>> getAllCategories() {
        return ResponseEntity.ok(supportFaqCategoryService.getAllCategories());
    }

    @Operation(summary = "FAQ 카테고리 단건 조회", description = "ID로 FAQ 카테고리 항목을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportFaqCategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(supportFaqCategoryService.getCategoryById(id));
    }

    @Operation(summary = "FAQ 카테고리 수정", description = "기존 FAQ 카테고리 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<SupportFaqCategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody SupportFaqCategoryRequestDto request) {
        return ResponseEntity.ok(supportFaqCategoryService.updateCategory(id, request));
    }

    @Operation(summary = "FAQ 카테고리 삭제", description = "FAQ 카테고리를 삭제합니다. (카테고리가 삽입된 FAQ 목록도 함께 삭제)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        supportFaqCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
