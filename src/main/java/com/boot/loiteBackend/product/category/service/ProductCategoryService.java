package com.boot.loiteBackend.product.category.service;

import com.boot.loiteBackend.product.category.dto.ProductCategoryRequestDto;
import com.boot.loiteBackend.product.category.dto.ProductCategoryResponseDto;

import java.util.List;

public interface ProductCategoryService {
    Long saveCategory(ProductCategoryRequestDto dto);
    List<ProductCategoryResponseDto> getAllCategory();
    void deleteCategory(Long categoryId);
}
