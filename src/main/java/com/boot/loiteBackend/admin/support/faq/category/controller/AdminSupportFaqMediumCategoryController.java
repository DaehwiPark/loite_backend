package com.boot.loiteBackend.admin.support.faq.category.controller;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryRequestDto;
import com.boot.loiteBackend.admin.support.faq.category.service.AdminSupportFaqMediumCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/support/faq/category/medium")
@Tag(name = "FAQ 중분류 카테고리 API", description = "FAQ 중분류 관리 API (대분류 선택 필수)")
public class AdminSupportFaqMediumCategoryController {

    private final AdminSupportFaqMediumCategoryService mediumCategoryService;

    @Operation(summary = "중분류 생성", description = "중분류 생성 시 반드시 대분류 ID를 포함해야 합니다.")
    @PostMapping
    public ResponseEntity<AdminSupportFaqMediumCategoryDto> createMedium(@RequestBody AdminSupportFaqMediumCategoryRequestDto request) {
        return ResponseEntity.status(201).body(mediumCategoryService.createCategory(request));
    }

    @Operation(summary = "중분류 전체 조회")
    @GetMapping
    public ResponseEntity<List<AdminSupportFaqMediumCategoryDto>> getAllMediums() {
        return ResponseEntity.ok(mediumCategoryService.getAllCategories());
    }

    @Operation(summary = "중분류 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<AdminSupportFaqMediumCategoryDto> getMediumById(@PathVariable Long id) {
        return ResponseEntity.ok(mediumCategoryService.getCategoryById(id));
    }

    @Operation(summary = "중분류 수정")
    @PutMapping("/{id}")
    public ResponseEntity<AdminSupportFaqMediumCategoryDto> updateMedium(
            @PathVariable Long id,
            @RequestBody AdminSupportFaqMediumCategoryRequestDto request) {
        return ResponseEntity.ok(mediumCategoryService.updateCategory(id, request));
    }

    @Operation(summary = "중분류 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedium(@PathVariable Long id) {
        mediumCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "대분류 ID 기준 중분류 조회")
    @GetMapping(params = "majorId")
    public ResponseEntity<List<AdminSupportFaqMediumCategoryDto>> getByMajorId(
            @RequestParam Long majorId
    ) {
        return ResponseEntity.ok(mediumCategoryService.getMediumsByMajorCategoryId(majorId));
    }

}
