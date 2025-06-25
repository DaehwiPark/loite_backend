package com.boot.loiteBackend.support.faq.general.service;


import com.boot.loiteBackend.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteBackend.support.faq.general.dto.SupportFaqRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupportFaqService {

    Page<SupportFaqDto> getPagedFaqs(String keyword, Pageable pageable);

    SupportFaqDto getFaqById(Long id);

    SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request);

    void deleteFaqById(Long id);

    SupportFaqDto createFaq(SupportFaqRequestDto request);
}
