package com.boot.loiteBackend.product.category.service;

import com.boot.loiteBackend.product.category.dto.AdminProductCategoryRequestDto;
import com.boot.loiteBackend.product.category.dto.AdminProductCategoryResponseDto;

import java.util.List;

public interface AdminProductCategoryService {
    Long saveCategory(AdminProductCategoryRequestDto dto);
    List<AdminProductCategoryResponseDto> getAllCategory();
    void deleteCategory(Long categoryId);
}
