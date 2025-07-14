package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryRequestDto;

import java.util.List;

public interface AdminSupportFaqMediumCategoryService {
    AdminSupportFaqCategoryDto createCategory(AdminSupportFaqMediumCategoryRequestDto request);
    List<AdminSupportFaqCategoryDto> getAllCategories();
    AdminSupportFaqCategoryDto getCategoryById(Long id);
    AdminSupportFaqCategoryDto updateCategory(Long id, AdminSupportFaqMediumCategoryRequestDto request);
    void deleteCategory(Long id);
}
