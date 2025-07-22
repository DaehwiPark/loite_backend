package com.boot.loiteBackend.web.support.resource.category.controller;

import com.boot.loiteBackend.web.support.resource.category.dto.SupportResourceCategoryDto;
import com.boot.loiteBackend.web.support.resource.category.service.SupportResourceCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/support/resource")
@Tag(name = "고객센터 자료실 카테고리 API", description = "자료실 카테고리 리스트 API")
public class SupportResourceCategoryController {

    private final SupportResourceCategoryService categoryService;

    @Operation(summary = "매뉴얼이 존재하는 유효한 카테고리 목록 조회", description = "삭제되지 않고 활성화된 상태이며 매뉴얼이 하나 이상 존재하는 카테고리 목록을 조회합니다.")
    @GetMapping("/categories")
    public ResponseEntity<List<SupportResourceCategoryDto>> getUsableCategories() {
        return ResponseEntity.ok(categoryService.getUsableCategories());
    }
}
