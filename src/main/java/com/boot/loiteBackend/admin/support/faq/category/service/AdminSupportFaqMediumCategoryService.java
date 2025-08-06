package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminSupportFaqMediumCategoryService {

    AdminSupportFaqMediumCategoryDto createCategory(AdminSupportFaqMediumCategoryRequestDto request, MultipartFile faqImage);

    List<AdminSupportFaqMediumCategoryDto> getAllCategories();

    AdminSupportFaqMediumCategoryDto getCategoryById(Long id);

    AdminSupportFaqMediumCategoryDto updateCategory(Long id, AdminSupportFaqMediumCategoryRequestDto request, MultipartFile faqImage);

    void deleteCategory(Long id);

    List<AdminSupportFaqMediumCategoryDto> getMediumsByMajorCategoryId(Long majorId);

}
