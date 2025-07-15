package com.boot.loiteBackend.web.support.faq.category.controller;

import com.boot.loiteBackend.web.support.faq.category.dto.SupportFaqMediumCategoryDto;
import com.boot.loiteBackend.web.support.faq.category.service.SupportFaqMediumCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/support/faq/category/medium")
@Tag(name = "고객센터 FAQ 중분류 카테고리 API", description = "FAQ 중분류 카테고리 조회 API")
public class SupportFaqMediumCategoryController {

    private final SupportFaqMediumCategoryService supportFaqCategoryService;

    @Operation(summary = "대분류 ID 기준 중분류 카테고리 조회", description = "대분류 ID에 해당하는 중분류 카테고리를 표시순서 기준으로 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SupportFaqMediumCategoryDto>> getMediumsByMajorId(
            @Parameter(description = "대분류 ID", example = "1", required = true)
            @RequestParam Long majorId
    ) {
        List<SupportFaqMediumCategoryDto> result = supportFaqCategoryService.getMediumsByMajorId(majorId);
        return ResponseEntity.ok(result);
    }
}
