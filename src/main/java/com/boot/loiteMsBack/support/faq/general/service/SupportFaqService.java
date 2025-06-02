package com.boot.loiteMsBack.support.faq.general.service;


import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqRequestDto;

import java.util.List;

public interface SupportFaqService {

    List<SupportFaqDto> getAllFaq();
    SupportFaqDto getFaqById(Long id);
    SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request);
    void deleteFaqById(Long id);

}
