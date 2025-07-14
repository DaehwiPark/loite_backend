package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryRequestDto;
import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqMediumCategoryEntity;
import com.boot.loiteBackend.admin.support.faq.category.error.AdminFaqCategoryErrorCode;
import com.boot.loiteBackend.admin.support.faq.category.repository.AdminSupportFaqCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSupportFaqMediumCategoryServiceImpl implements AdminSupportFaqMediumCategoryService {

    private final AdminSupportFaqCategoryRepository adminSupportFaqcategoryRepository;

    @Override
    @Transactional
    public AdminSupportFaqCategoryDto createCategory(AdminSupportFaqMediumCategoryRequestDto request) {
        AdminSupportFaqMediumCategoryEntity entity = AdminSupportFaqMediumCategoryEntity.builder()
                .faqCategoryName(request.getFaqCategoryName())
                .faqCategoryOrder(request.getFaqCategoryOrder())
                .build();
        adminSupportFaqcategoryRepository.save(entity);
        return new AdminSupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminSupportFaqCategoryDto> getAllCategories() {
        return adminSupportFaqcategoryRepository.findAll().stream()
                .map(AdminSupportFaqCategoryDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSupportFaqCategoryDto getCategoryById(Long id) {
        AdminSupportFaqMediumCategoryEntity entity = adminSupportFaqcategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.NOT_FOUND));
        return new AdminSupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportFaqCategoryDto updateCategory(Long id, AdminSupportFaqMediumCategoryRequestDto request) {
        AdminSupportFaqMediumCategoryEntity entity = adminSupportFaqcategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.NOT_FOUND));

        entity.setFaqCategoryName(request.getFaqCategoryName());
        entity.setFaqCategoryOrder(request.getFaqCategoryOrder());

        adminSupportFaqcategoryRepository.save(entity);
        return new AdminSupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!adminSupportFaqcategoryRepository.existsById(id)) {
            throw new CustomException(AdminFaqCategoryErrorCode.DELETE_FAILED);
        }
        adminSupportFaqcategoryRepository.deleteById(id);
    }
}