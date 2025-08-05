package com.boot.loiteBackend.web.support.faq.category.controller;

import com.boot.loiteBackend.web.support.faq.category.dto.SupportFaqMajorCategoryDto;
import com.boot.loiteBackend.web.support.faq.category.service.SupportFaqMajorCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/support/faq/category/major")
@Tag(name = "고객센터 FAQ 관련 API", description = "FAQ 대분류/중분류 카테고리 및 목록 조회 API")
public class SupportFaqMajorCategoryController {

    private final SupportFaqMajorCategoryService supportFaqCategoryService;

    @Operation(summary = "대분류 카테고리 목록 조회", description = "표시순서(오름차순)에 따라 정렬된 대분류 카테고리 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SupportFaqMajorCategoryDto>> getAllMajors() {
        List<SupportFaqMajorCategoryDto> result = supportFaqCategoryService.getAllMajorsOrdered();
        return ResponseEntity.ok(result);
    }
}
