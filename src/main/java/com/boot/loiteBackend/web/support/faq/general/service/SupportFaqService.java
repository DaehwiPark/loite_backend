package com.boot.loiteBackend.web.support.faq.general.service;

import com.boot.loiteBackend.web.support.faq.general.dto.SupportFaqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupportFaqService {

    Page<SupportFaqDto> getFaqsByMediumCategory(Long mediumId, Pageable pageable);

    Page<SupportFaqDto> getFaqsByMajorCategory(Long majorId, Pageable pageable);
}