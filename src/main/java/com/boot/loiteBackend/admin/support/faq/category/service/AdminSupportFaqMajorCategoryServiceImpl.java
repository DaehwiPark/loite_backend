package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMajorCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMajorCategoryRequestDto;
import com.boot.loiteBackend.admin.support.faq.category.error.AdminFaqCategoryErrorCode;
import com.boot.loiteBackend.admin.support.faq.category.repository.AdminSupportFaqMajorCategoryRepository;
import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMajorCategoryEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSupportFaqMajorCategoryServiceImpl implements AdminSupportFaqMajorCategoryService {

    private final AdminSupportFaqMajorCategoryRepository majorCategoryRepository;

    @Override
    @Transactional
    public AdminSupportFaqMajorCategoryDto createCategory(AdminSupportFaqMajorCategoryRequestDto request) {
        SupportFaqMajorCategoryEntity entity = SupportFaqMajorCategoryEntity.builder()
                .faqMajorCategoryName(request.getFaqMajorCategoryName())
                .faqMajorCategoryOrder(request.getFaqMajorCategoryOrder())
                .build();
        majorCategoryRepository.save(entity);
        return new AdminSupportFaqMajorCategoryDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminSupportFaqMajorCategoryDto> getAllCategories() {
        return majorCategoryRepository.findAll().stream()
                .map(AdminSupportFaqMajorCategoryDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSupportFaqMajorCategoryDto getCategoryById(Long id) {
        SupportFaqMajorCategoryEntity entity = majorCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.MAJOR_CATEGORY_NOT_FOUND));
        return new AdminSupportFaqMajorCategoryDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportFaqMajorCategoryDto updateCategory(Long id, AdminSupportFaqMajorCategoryRequestDto request) {
        SupportFaqMajorCategoryEntity entity = majorCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.MAJOR_CATEGORY_NOT_FOUND));

        entity.setFaqMajorCategoryName(request.getFaqMajorCategoryName());
        entity.setFaqMajorCategoryOrder(request.getFaqMajorCategoryOrder());

        return new AdminSupportFaqMajorCategoryDto(entity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!majorCategoryRepository.existsById(id)) {
            throw new CustomException(AdminFaqCategoryErrorCode.MAJOR_CATEGORY_DELETE_FAILED);
        }
        majorCategoryRepository.deleteById(id);
    }
}
