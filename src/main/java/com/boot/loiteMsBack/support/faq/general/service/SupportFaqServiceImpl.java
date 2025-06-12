package com.boot.loiteMsBack.support.faq.general.service;

import com.boot.loiteMsBack.global.error.exception.CustomException;
import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqRequestDto;
import com.boot.loiteMsBack.support.faq.general.entity.SupportFaqEntity;
import com.boot.loiteMsBack.support.faq.general.error.FaqErrorCode;
import com.boot.loiteMsBack.support.faq.general.repository.SupportFaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportFaqServiceImpl implements SupportFaqService {

    private final SupportFaqRepository supportFaqRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SupportFaqDto> getPagedFaqs(String keyword, Pageable pageable) {
        Page<SupportFaqEntity> page;
        if (StringUtils.hasText(keyword)) {
            page = supportFaqRepository.findByKeyword(keyword, pageable);
        } else {
            page = supportFaqRepository.findAll(pageable);
        }
        return page.map(this::toDto);
    }


    @Override
    public SupportFaqDto getFaqById(Long id) {
        SupportFaqEntity entity = supportFaqRepository.findById(id)
                .orElseThrow(() -> new CustomException(FaqErrorCode.NOT_FOUND));
        return toDto(entity);
    }

    @Override
    public SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request) {
        SupportFaqEntity entity = supportFaqRepository.findById(id)
                .orElseThrow(() -> new CustomException(FaqErrorCode.NOT_FOUND));
        entity.setFaqQuestion(request.getFaqQuestion());
        entity.setFaqAnswer(request.getFaqAnswer());
        supportFaqRepository.save(entity);
        return toDto(entity);
    }

    @Override
    @Transactional
    public void deleteFaqById(Long id) {
        if (!supportFaqRepository.existsById(id)) {
            throw new CustomException(FaqErrorCode.DELETE_FAILED);
        }
        supportFaqRepository.deleteById(id);
    }

    private SupportFaqDto toDto(SupportFaqEntity entity) {
        return SupportFaqDto.builder()
                .faqId(entity.getFaqId())
                .faqQuestion(entity.getFaqQuestion())
                .faqAnswer(entity.getFaqAnswer())
                .faqCategoryName(entity.getFaqCategory() != null ? entity.getFaqCategory().getFaqCategoryName() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
