package com.boot.loiteBackend.support.faq.category.service;

import com.boot.loiteBackend.support.faq.category.dto.SupportFaqCategoryDto;
import com.boot.loiteBackend.support.faq.category.dto.SupportFaqCategoryRequestDto;

import java.util.List;

public interface SupportFaqCategoryService {
    SupportFaqCategoryDto createCategory(SupportFaqCategoryRequestDto request);
    List<SupportFaqCategoryDto> getAllCategories();
    SupportFaqCategoryDto getCategoryById(Long id);
    SupportFaqCategoryDto updateCategory(Long id, SupportFaqCategoryRequestDto request);
    void deleteCategory(Long id);
}
