package com.boot.loiteBackend.web.category.controller;

import com.boot.loiteBackend.admin.product.category.dto.AdminProductCategoryResponseDto;
import com.boot.loiteBackend.admin.product.category.service.AdminProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/product/category")
@Tag(name = "카테고리 조회", description = "카테고리 조회 API")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final AdminProductCategoryService adminProductCategoryService;

    @Operation(summary = "카테고리 조회", description = "카테고리를 트리구조로 조회합니다.")
    @GetMapping("/tree")
    public List<AdminProductCategoryResponseDto> getCategory()
    {
        return adminProductCategoryService.getAllCategory();
    }
}