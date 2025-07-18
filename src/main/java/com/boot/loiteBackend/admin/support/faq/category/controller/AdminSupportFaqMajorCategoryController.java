package com.boot.loiteBackend.admin.support.faq.category.controller;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMajorCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMajorCategoryRequestDto;
import com.boot.loiteBackend.admin.support.faq.category.service.AdminSupportFaqMajorCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/support/faq/category/major")
@Tag(name = "FAQ 대분류 카테고리 API", description = "고객센터 FAQ 대분류 카테고리 관리 API")
public class AdminSupportFaqMajorCategoryController {

    private final AdminSupportFaqMajorCategoryService majorCategoryService;

    @Operation(summary = "대분류 카테고리 생성", description = "새로운 FAQ 대분류 카테고리를 생성합니다.")
    @PostMapping
    public ResponseEntity<AdminSupportFaqMajorCategoryDto> create(@RequestBody AdminSupportFaqMajorCategoryRequestDto request) {
        return ResponseEntity.status(201).body(majorCategoryService.createCategory(request));
    }

    @Operation(summary = "대분류 카테고리 전체 조회", description = "모든 FAQ 대분류 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<AdminSupportFaqMajorCategoryDto>> getAll() {
        return ResponseEntity.ok(majorCategoryService.getAllCategories());
    }

    @Operation(summary = "대분류 카테고리 단건 조회", description = "ID를 기반으로 FAQ 대분류 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<AdminSupportFaqMajorCategoryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(majorCategoryService.getCategoryById(id));
    }

    @Operation(summary = "대분류 카테고리 수정", description = "기존 FAQ 대분류 카테고리 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<AdminSupportFaqMajorCategoryDto> update(
            @PathVariable Long id,
            @RequestBody AdminSupportFaqMajorCategoryRequestDto request) {
        return ResponseEntity.ok(majorCategoryService.updateCategory(id, request));
    }

    @Operation(summary = "대분류 카테고리 삭제", description = "FAQ 대분류 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        majorCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
