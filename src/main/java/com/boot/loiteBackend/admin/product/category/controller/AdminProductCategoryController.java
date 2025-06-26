package com.boot.loiteBackend.product.category.controller;

import com.boot.loiteBackend.product.category.dto.AdminProductCategoryRequestDto;
import com.boot.loiteBackend.product.category.dto.AdminProductCategoryResponseDto;
import com.boot.loiteBackend.product.category.service.AdminProductCategoryService;
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

    @PostMapping
    public ResponseEntity<Long> saveCategory(@RequestBody @Valid AdminProductCategoryRequestDto dto){
        Long saveId = adminProductCategoryService.saveCategory(dto);

        return ResponseEntity.ok(saveId);
    }

    @GetMapping("/tree")
    public List<AdminProductCategoryResponseDto> getCategory(){
        return adminProductCategoryService.getAllCategory();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        adminProductCategoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
    }

}
