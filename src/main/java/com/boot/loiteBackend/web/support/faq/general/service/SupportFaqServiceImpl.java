package com.boot.loiteBackend.web.support.faq.general.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.support.faq.category.repository.SupportFaqMediumCategoryRepository;
import com.boot.loiteBackend.web.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteBackend.web.support.faq.general.entity.SupportFaqEntity;
import com.boot.loiteBackend.web.support.faq.general.repository.SupportFaqRepository;
import com.boot.loiteBackend.web.support.faq.general.error.SupportFaqErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportFaqServiceImpl implements SupportFaqService {

    private final SupportFaqRepository faqRepository;
    private final SupportFaqMediumCategoryRepository supportFaqMediumCategoryRepository;


    @Override
    public Page<SupportFaqDto> getFaqsByMediumCategory(Long mediumId, Pageable pageable) {
        if (mediumId == null) {
            throw new CustomException(SupportFaqErrorCode.INVALID_PARAMETER);
        }
        Page<SupportFaqEntity> page = faqRepository.findByFaqCategory_FaqMediumCategoryIdAndDeleteYn(mediumId, "N", pageable);
        return page.map(SupportFaqDto::from);
    }

    @Override
    public Page<SupportFaqDto> getFaqsByMajorCategory(Long majorId, Pageable pageable, String keyword) {
        if (majorId == null) {
            throw new CustomException(SupportFaqErrorCode.INVALID_PARAMETER);
        }

        List<Long> mediumIds = supportFaqMediumCategoryRepository.findIdsByMajorId(majorId);

        if (mediumIds == null || mediumIds.isEmpty()) {
            return Page.empty();
        }

        Page<SupportFaqEntity> page = faqRepository.searchByMediumCategoryIdsWithKeyword(mediumIds, keyword, pageable);

        return page.map(SupportFaqDto::from);
    }
}
