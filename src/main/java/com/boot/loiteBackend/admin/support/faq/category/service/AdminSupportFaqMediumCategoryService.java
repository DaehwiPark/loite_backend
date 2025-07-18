package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryRequestDto;

import java.util.List;

public interface AdminSupportFaqMediumCategoryService {

    AdminSupportFaqMediumCategoryDto createCategory(AdminSupportFaqMediumCategoryRequestDto request);

    List<AdminSupportFaqMediumCategoryDto> getAllCategories();

    AdminSupportFaqMediumCategoryDto getCategoryById(Long id);

    AdminSupportFaqMediumCategoryDto updateCategory(Long id, AdminSupportFaqMediumCategoryRequestDto request);

    void deleteCategory(Long id);

    List<AdminSupportFaqMediumCategoryDto> getMediumsByMajorCategoryId(Long majorId);

}
