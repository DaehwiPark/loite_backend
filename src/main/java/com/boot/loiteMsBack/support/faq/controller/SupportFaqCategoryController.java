package com.boot.loiteMsBack.support.faq.controller;

import com.boot.loiteMsBack.support.faq.dto.SupportFaqCategoryDto;
import com.boot.loiteMsBack.support.faq.dto.SupportFaqCategoryRequestDto;
import com.boot.loiteMsBack.support.faq.service.SupportFaqCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support/faqs/category")
@Tag(name = "Support FAQ Category", description = "자주 묻는 질문(FAQ) 카테고리 관련 API")
public class SupportFaqCategoryController {

    private final SupportFaqCategoryService supportFaqCategoryService;

    public SupportFaqCategoryController(SupportFaqCategoryService supportFaqCategoryService) {
        this.supportFaqCategoryService = supportFaqCategoryService;
    }

    @Operation(summary = "FAQ 카테고리 생성", description = "새로운 FAQ 카테고리 항목을 생성합니다.")
    @PostMapping
    public SupportFaqCategoryDto createCategory(@RequestBody SupportFaqCategoryRequestDto request) {
        return supportFaqCategoryService.createCategory(request);
    }

    @Operation(summary = "FAQ 카테고리 전체 조회", description = "등록된 모든 FAQ 카테고리를 조회합니다.")
    @GetMapping
    public List<SupportFaqCategoryDto> getAllCategories() {
        return supportFaqCategoryService.getAllCategories();
    }

    @Operation(summary = "FAQ 카테고리 단건 조회", description = "ID로 FAQ 카테고리 항목을 조회합니다.")
    @GetMapping("/{id}")
    public SupportFaqCategoryDto getCategoryById(@PathVariable Long id) {
        return supportFaqCategoryService.getCategoryById(id);
    }

    @Operation(summary = "FAQ 카테고리 수정", description = "기존 FAQ 카테고리 정보를 수정합니다.")
    @PutMapping("/{id}")
    public SupportFaqCategoryDto updateCategory(@PathVariable Long id, @RequestBody SupportFaqCategoryRequestDto request) {
        return supportFaqCategoryService.updateCategory(id, request);
    }

    @Operation(summary = "FAQ 카테고리 삭제", description = "FAQ 카테고리를 삭제합니다. (카테고리가 삽입된 FAQ 목록도 함께 삭제)")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        supportFaqCategoryService.deleteCategory(id);
    }
}
