package com.boot.loiteBackend.admin.product.category.service;

import com.boot.loiteBackend.admin.product.category.dto.AdminProductCategoryRequestDto;
import com.boot.loiteBackend.admin.product.category.dto.AdminProductCategoryResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminProductCategoryService {
    Long saveCategory(AdminProductCategoryRequestDto dto, MultipartFile imageFile);
    List<AdminProductCategoryResponseDto> getAllCategory();
    void deleteCategory(Long categoryId);
}
