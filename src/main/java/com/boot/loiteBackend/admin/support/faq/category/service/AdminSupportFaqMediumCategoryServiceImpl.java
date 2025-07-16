package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryRequestDto;
import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqMajorCategoryEntity;
import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqMediumCategoryEntity;
import com.boot.loiteBackend.admin.support.faq.category.error.AdminFaqCategoryErrorCode;
import com.boot.loiteBackend.admin.support.faq.category.repository.AdminSupportFaqMajorCategoryRepository;
import com.boot.loiteBackend.admin.support.faq.category.repository.AdminSupportFaqMediumCategoryRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSupportFaqMediumCategoryServiceImpl implements AdminSupportFaqMediumCategoryService {

    private final AdminSupportFaqMediumCategoryRepository mediumCategoryRepository;
    private final AdminSupportFaqMajorCategoryRepository majorCategoryRepository;

    @Override
    @Transactional
    public AdminSupportFaqMediumCategoryDto createCategory(AdminSupportFaqMediumCategoryRequestDto request) {
        AdminSupportFaqMajorCategoryEntity majorCategory = majorCategoryRepository.findById(request.getFaqMajorCategoryId())
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.MAJOR_CATEGORY_NOT_FOUND));

        AdminSupportFaqMediumCategoryEntity entity = AdminSupportFaqMediumCategoryEntity.builder()
                .faqMediumCategoryName(request.getFaqMediumCategoryName())
                .faqMediumCategoryOrder(request.getFaqMediumCategoryOrder())
                .faqMajorCategory(majorCategory)
                .build();

        mediumCategoryRepository.save(entity);
        return new AdminSupportFaqMediumCategoryDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminSupportFaqMediumCategoryDto> getAllCategories() {
        return mediumCategoryRepository.findAll().stream()
                .map(AdminSupportFaqMediumCategoryDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSupportFaqMediumCategoryDto getCategoryById(Long id) {
        AdminSupportFaqMediumCategoryEntity entity = mediumCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.NOT_FOUND));
        return new AdminSupportFaqMediumCategoryDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportFaqMediumCategoryDto updateCategory(Long id, AdminSupportFaqMediumCategoryRequestDto request) {
        AdminSupportFaqMediumCategoryEntity entity = mediumCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.NOT_FOUND));

        AdminSupportFaqMajorCategoryEntity majorCategory = majorCategoryRepository.findById(request.getFaqMajorCategoryId())
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.MAJOR_CATEGORY_NOT_FOUND));

        entity.setFaqMediumCategoryName(request.getFaqMediumCategoryName());
        entity.setFaqMediumCategoryOrder(request.getFaqMediumCategoryOrder());
        entity.setFaqMajorCategory(majorCategory);

        return new AdminSupportFaqMediumCategoryDto(entity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!mediumCategoryRepository.existsById(id)) {
            throw new CustomException(AdminFaqCategoryErrorCode.DELETE_FAILED);
        }
        mediumCategoryRepository.deleteById(id);
    }


    @Override
    public List<AdminSupportFaqMediumCategoryDto> getMediumsByMajorCategoryId(Long majorId) {
        List<AdminSupportFaqMediumCategoryEntity> entities =
                mediumCategoryRepository.findByFaqMajorCategory_FaqMajorCategoryId(majorId);

        return entities.stream()
                .map(entity -> AdminSupportFaqMediumCategoryDto.builder()
                        .faqMediumCategoryId(entity.getFaqMediumCategoryId())
                        .faqMediumCategoryName(entity.getFaqMediumCategoryName())
                        .faqMediumCategoryOrder(entity.getFaqMediumCategoryOrder())
                        .faqMajorCategoryId(entity.getFaqMajorCategory().getFaqMajorCategoryId())
                        .faqMajorCategoryName(entity.getFaqMajorCategory().getFaqMajorCategoryName())
                        .build())
                .collect(Collectors.toList());
    }

}
