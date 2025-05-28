package com.boot.loiteMsBack.support.faq.service;


import com.boot.loiteMsBack.support.faq.dto.SupportFaqRequestDto;
import com.boot.loiteMsBack.support.faq.dto.SupportFaqDto;

import java.util.List;

public interface SupportFaqService {

    List<SupportFaqDto> getAllFaq();

    SupportFaqDto getFaqById(Long id);

    SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request);

    void deleteFaqById(Long id);

}
