package com.boot.loiteMsBack.support.faq.service;

import com.boot.loiteMsBack.support.faq.dto.SupportFaqCategoryDto;
import com.boot.loiteMsBack.support.faq.dto.SupportFaqCategoryRequestDto;

import java.util.List;

public interface SupportFaqCategoryService {

    SupportFaqCategoryDto createCategory(SupportFaqCategoryRequestDto request);

    List<SupportFaqCategoryDto> getAllCategories();

    SupportFaqCategoryDto getCategoryById(Long id);

    SupportFaqCategoryDto updateCategory(Long id, SupportFaqCategoryRequestDto request);

    void deleteCategory(Long id);
}
