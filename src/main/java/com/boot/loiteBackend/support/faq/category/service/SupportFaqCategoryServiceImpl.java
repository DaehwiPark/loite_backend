package com.boot.loiteBackend.support.faq.category.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.support.faq.category.dto.SupportFaqCategoryDto;
import com.boot.loiteBackend.support.faq.category.dto.SupportFaqCategoryRequestDto;
import com.boot.loiteBackend.support.faq.category.entity.SupportFaqCategoryEntity;
import com.boot.loiteBackend.support.faq.category.error.FaqCategoryErrorCode;
import com.boot.loiteBackend.support.faq.category.repository.SupportFaqCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportFaqCategoryServiceImpl implements SupportFaqCategoryService {

    private final SupportFaqCategoryRepository supportFaqcategoryRepository;

    @Override
    @Transactional
    public SupportFaqCategoryDto createCategory(SupportFaqCategoryRequestDto request) {
        SupportFaqCategoryEntity entity = SupportFaqCategoryEntity.builder()
                .faqCategoryName(request.getFaqCategoryName())
                .faqCategoryOrder(request.getFaqCategoryOrder())
                .build();
        supportFaqcategoryRepository.save(entity);
        return new SupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportFaqCategoryDto> getAllCategories() {
        return supportFaqcategoryRepository.findAll().stream()
                .map(SupportFaqCategoryDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupportFaqCategoryDto getCategoryById(Long id) {
        SupportFaqCategoryEntity entity = supportFaqcategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(FaqCategoryErrorCode.NOT_FOUND));
        return new SupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional
    public SupportFaqCategoryDto updateCategory(Long id, SupportFaqCategoryRequestDto request) {
        SupportFaqCategoryEntity entity = supportFaqcategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(FaqCategoryErrorCode.NOT_FOUND));

        entity.setFaqCategoryName(request.getFaqCategoryName());
        entity.setFaqCategoryOrder(request.getFaqCategoryOrder());

        supportFaqcategoryRepository.save(entity);
        return new SupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!supportFaqcategoryRepository.existsById(id)) {
            throw new CustomException(FaqCategoryErrorCode.DELETE_FAILED);
        }
        supportFaqcategoryRepository.deleteById(id);
    }
}