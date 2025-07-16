package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMajorCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMajorCategoryRequestDto;

import java.util.List;

public interface AdminSupportFaqMajorCategoryService {

    AdminSupportFaqMajorCategoryDto createCategory(AdminSupportFaqMajorCategoryRequestDto request);

    List<AdminSupportFaqMajorCategoryDto> getAllCategories();

    AdminSupportFaqMajorCategoryDto getCategoryById(Long id);

    AdminSupportFaqMajorCategoryDto updateCategory(Long id, AdminSupportFaqMajorCategoryRequestDto request);

    void deleteCategory(Long id);
}
