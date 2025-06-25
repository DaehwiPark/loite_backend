package com.boot.loiteBackend.admin.product.category.service;

import com.boot.loiteBackend.admin.product.category.dto.ProductCategoryRequestDto;
import com.boot.loiteBackend.admin.product.category.dto.ProductCategoryResponseDto;

import java.util.List;

public interface ProductCategoryService {
    Long saveCategory(ProductCategoryRequestDto dto);
    List<ProductCategoryResponseDto> getAllCategory();
    void deleteCategory(Long categoryId);
}
