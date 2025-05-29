package com.boot.loiteMsBack.support.faq.question.service;


import com.boot.loiteMsBack.support.faq.question.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.faq.question.dto.SupportFaqRequestDto;

import java.util.List;

public interface SupportFaqService {

    List<SupportFaqDto> getAllFaq();
    SupportFaqDto getFaqById(Long id);
    SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request);
    void deleteFaqById(Long id);

}
