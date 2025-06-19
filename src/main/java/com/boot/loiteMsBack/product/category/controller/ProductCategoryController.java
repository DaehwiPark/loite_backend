package com.boot.loiteMsBack.product.category.controller;

import com.boot.loiteMsBack.product.category.dto.ProductCategoryRequestDto;
import com.boot.loiteMsBack.product.category.dto.ProductCategoryResponseDto;
import com.boot.loiteMsBack.product.category.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/category")
@Tag(name = "Category Management", description = "카테고리 관리 API")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<Long> saveCategory(@RequestBody @Valid ProductCategoryRequestDto dto){
        Long saveId = productCategoryService.saveCategory(dto);

        return ResponseEntity.ok(saveId);
    }

    @GetMapping("/tree")
    public List<ProductCategoryResponseDto> getCategory(){
        return productCategoryService.getAllCategory();
    }
}
