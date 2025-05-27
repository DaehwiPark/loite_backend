package com.boot.loiteMsBack.support.faq.service;

import com.boot.loiteMsBack.support.faq.dto.SupportFaqCategoryDto;
import com.boot.loiteMsBack.support.faq.dto.SupportFaqCategoryRequestDto;
import com.boot.loiteMsBack.support.faq.entity.SupportFaqCategoryEntity;
import com.boot.loiteMsBack.support.faq.repository.SupportFaqCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportFaqCategoryServiceImpl implements SupportFaqCategoryService {

    private final SupportFaqCategoryRepository supportFaqcategoryRepository;

    public SupportFaqCategoryServiceImpl(SupportFaqCategoryRepository supportFaqcategoryRepository) {
        this.supportFaqcategoryRepository = supportFaqcategoryRepository;
    }

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
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return new SupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional
    public SupportFaqCategoryDto updateCategory(Long id, SupportFaqCategoryRequestDto request) {
        SupportFaqCategoryEntity entity = supportFaqcategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        entity.setFaqCategoryName(request.getFaqCategoryName());
        entity.setFaqCategoryOrder(request.getFaqCategoryOrder());

        supportFaqcategoryRepository.save(entity);
        return new SupportFaqCategoryDto(entity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!supportFaqcategoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        supportFaqcategoryRepository.deleteById(id);
    }
}