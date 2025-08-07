package com.boot.loiteBackend.admin.support.faq.general.service;

import com.boot.loiteBackend.admin.support.faq.category.repository.AdminSupportFaqMediumCategoryRepository;
import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import com.boot.loiteBackend.domain.support.faq.general.entity.SupportFaqEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqDto;
import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqRequestDto;
import com.boot.loiteBackend.admin.support.faq.general.error.AdminFaqErrorCode;
import com.boot.loiteBackend.admin.support.faq.general.mapper.AdminSupportFaqMapper;
import com.boot.loiteBackend.admin.support.faq.general.repository.AdminSupportFaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AdminSupportFaqServiceImpl implements AdminSupportFaqService {

    private final AdminSupportFaqRepository adminSupportFaqRepository;
    private final AdminSupportFaqMediumCategoryRepository faqCategoryRepository;
    private final AdminSupportFaqMapper adminSupportFaqMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminSupportFaqDto> getPagedFaqs(String keyword, Pageable pageable) {
        Page<SupportFaqEntity> page;
        if (StringUtils.hasText(keyword)) {
            page = adminSupportFaqRepository.findByKeyword(keyword, pageable);
        } else {
            page = adminSupportFaqRepository.findAll(pageable);
        }
        return page.map(adminSupportFaqMapper::toDto);
    }

    @Override
    @Transactional
    public AdminSupportFaqDto createFaq(AdminSupportFaqRequestDto request) {
        SupportFaqMediumCategoryEntity category = faqCategoryRepository.findById(request.getFaqMediumCategoryId())
                .orElseThrow(() -> new CustomException(AdminFaqErrorCode.CATEGORY_NOT_FOUND));

        SupportFaqEntity faq = SupportFaqEntity.builder()
                .faqQuestion(request.getFaqQuestion())
                .faqAnswer(request.getFaqAnswer())
                .faqCategory(category)
                .displayYn(request.getDisplayYn() != null ? request.getDisplayYn() : "Y")
                .deleteYn("N")
                .build();

        SupportFaqEntity saved = adminSupportFaqRepository.save(faq);
        return adminSupportFaqMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSupportFaqDto getFaqById(Long id) {
        SupportFaqEntity entity = adminSupportFaqRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqErrorCode.NOT_FOUND));
        return adminSupportFaqMapper.toDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportFaqDto updateFaq(Long id, AdminSupportFaqRequestDto request) {
        SupportFaqEntity entity = adminSupportFaqRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqErrorCode.NOT_FOUND));

        SupportFaqMediumCategoryEntity category = faqCategoryRepository.findById(request.getFaqMediumCategoryId())
                .orElseThrow(() -> new CustomException(AdminFaqErrorCode.CATEGORY_NOT_FOUND));

        entity.setFaqCategory(category);
        entity.setFaqQuestion(request.getFaqQuestion());
        entity.setFaqAnswer(request.getFaqAnswer());
        entity.setDisplayYn(request.getDisplayYn());

        adminSupportFaqRepository.save(entity);
        return adminSupportFaqMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteFaqById(Long id) {
        if (!adminSupportFaqRepository.existsById(id)) {
            throw new CustomException(AdminFaqErrorCode.DELETE_FAILED);
        }
        adminSupportFaqRepository.deleteById(id);
    }
}
