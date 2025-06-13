package com.boot.loiteMsBack.support.faq.general.service;

import com.boot.loiteMsBack.global.error.exception.CustomException;
import com.boot.loiteMsBack.support.faq.category.entity.SupportFaqCategoryEntity;
import com.boot.loiteMsBack.support.faq.category.repository.SupportFaqCategoryRepository;
import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqRequestDto;
import com.boot.loiteMsBack.support.faq.general.entity.SupportFaqEntity;
import com.boot.loiteMsBack.support.faq.general.error.FaqErrorCode;
import com.boot.loiteMsBack.support.faq.general.mapper.SupportFaqMapper;
import com.boot.loiteMsBack.support.faq.general.repository.SupportFaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SupportFaqServiceImpl implements SupportFaqService {

    private final SupportFaqRepository supportFaqRepository;
    private final SupportFaqCategoryRepository faqCategoryRepository;
    private final SupportFaqMapper supportFaqMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SupportFaqDto> getPagedFaqs(String keyword, Pageable pageable) {
        Page<SupportFaqEntity> page;
        if (StringUtils.hasText(keyword)) {
            page = supportFaqRepository.findByKeyword(keyword, pageable);
        } else {
            page = supportFaqRepository.findAll(pageable);
        }
        return page.map(supportFaqMapper::toDto);
    }

    @Override
    @Transactional
    public SupportFaqDto createFaq(SupportFaqRequestDto request) {
        SupportFaqCategoryEntity category = faqCategoryRepository.findById(request.getFaqCategoryId())
                .orElseThrow(() -> new CustomException(FaqErrorCode.CATEGORY_NOT_FOUND));

        SupportFaqEntity faq = SupportFaqEntity.builder()
                .faqQuestion(request.getFaqQuestion())
                .faqAnswer(request.getFaqAnswer())
                .faqCategory(category)
                .displayYn(request.getDisplayYn() != null ? request.getDisplayYn() : "Y")
                .deleteYn("N")
                .build();

        SupportFaqEntity saved = supportFaqRepository.save(faq);
        return supportFaqMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportFaqDto getFaqById(Long id) {
        SupportFaqEntity entity = supportFaqRepository.findById(id)
                .orElseThrow(() -> new CustomException(FaqErrorCode.NOT_FOUND));
        return supportFaqMapper.toDto(entity);
    }

    @Override
    @Transactional
    public SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request) {
        SupportFaqEntity entity = supportFaqRepository.findById(id)
                .orElseThrow(() -> new CustomException(FaqErrorCode.NOT_FOUND));
        SupportFaqCategoryEntity newCategory = faqCategoryRepository.findById(request.getFaqCategoryId())
                .orElseThrow(() -> new CustomException(FaqErrorCode.CATEGORY_NOT_FOUND));
        entity.setFaqCategory(newCategory);
        entity.setFaqQuestion(request.getFaqQuestion());
        entity.setFaqAnswer(request.getFaqAnswer());
        entity.setDisplayYn(request.getDisplayYn());

        supportFaqRepository.save(entity);
        return supportFaqMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteFaqById(Long id) {
        if (!supportFaqRepository.existsById(id)) {
            throw new CustomException(FaqErrorCode.DELETE_FAILED);
        }
        supportFaqRepository.deleteById(id);
    }
}
