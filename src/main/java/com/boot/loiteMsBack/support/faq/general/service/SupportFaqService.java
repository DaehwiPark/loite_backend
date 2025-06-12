package com.boot.loiteMsBack.support.faq.general.service;


import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqRequestDto;
import com.boot.loiteMsBack.support.faq.general.entity.SupportFaqEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupportFaqService {

    Page<SupportFaqDto> getPagedFaqs(String keyword, Pageable pageable);

    SupportFaqDto getFaqById(Long id);

    SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request);

    void deleteFaqById(Long id);

}
