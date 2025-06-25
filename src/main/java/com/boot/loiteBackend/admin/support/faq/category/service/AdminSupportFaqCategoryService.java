package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqCategoryRequestDto;

import java.util.List;

public interface AdminSupportFaqCategoryService {
    AdminSupportFaqCategoryDto createCategory(AdminSupportFaqCategoryRequestDto request);
    List<AdminSupportFaqCategoryDto> getAllCategories();
    AdminSupportFaqCategoryDto getCategoryById(Long id);
    AdminSupportFaqCategoryDto updateCategory(Long id, AdminSupportFaqCategoryRequestDto request);
    void deleteCategory(Long id);
}
