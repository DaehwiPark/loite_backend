package com.boot.loiteBackend.web.support.faq.general.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteBackend.web.support.faq.general.entity.SupportFaqEntity;
import com.boot.loiteBackend.web.support.faq.general.repository.SupportFaqRepository;
import com.boot.loiteBackend.web.support.faq.general.error.SupportFaqErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportFaqServiceImpl implements SupportFaqService {

    private final SupportFaqRepository faqRepository;

    @Override
    public Page<SupportFaqDto> getFaqsByMediumCategory(Long mediumCategoryId, Pageable pageable) {
        if (mediumCategoryId == null) {
            throw new CustomException(SupportFaqErrorCode.INVALID_PARAMETER);
        }

        Page<SupportFaqEntity> page = faqRepository.findByFaqCategory_FaqMediumCategoryIdAndDeleteYn(mediumCategoryId, "N", pageable);
        return page.map(SupportFaqDto::from);
    }
}
