package com.boot.loiteBackend.admin.product.category.controller;

import com.boot.loiteBackend.admin.product.category.dto.AdminProductCategoryRequestDto;
import com.boot.loiteBackend.admin.product.category.dto.AdminProductCategoryResponseDto;
import com.boot.loiteBackend.admin.product.category.service.AdminProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/product/category")
@Tag(name = "Category Management", description = "카테고리 관리 API")
public class AdminProductCategoryController {
    private final AdminProductCategoryService adminProductCategoryService;

    @Operation(summary = "카테고리 등록", description = "카테고리를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> saveCategory(@RequestBody @Valid AdminProductCategoryRequestDto dto){
        Long saveId = adminProductCategoryService.saveCategory(dto);

        return ResponseEntity.ok(saveId);
    }

    @Operation(summary = "카테고리 조회", description = "카테고리를 트리구조로 조회합니다.")
    @GetMapping("/tree")
    public List<AdminProductCategoryResponseDto> getCategory()
    {
        return adminProductCategoryService.getAllCategory();
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        adminProductCategoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
    }

}
