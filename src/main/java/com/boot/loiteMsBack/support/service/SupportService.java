package com.boot.loiteMsBack.support.service;


import com.boot.loiteMsBack.support.dto.SupportFaqDto;

import java.util.List;

public interface SupportService {

    List<SupportFaqDto> getAllFaqDtos();

    SupportFaqDto getFaqDtoById(Long id);

    List<SupportFaqDto> getUnansweredFaqDtos();

    SupportFaqDto addAnswerToFaq(Long id, String answerContent);

    SupportFaqDto updateFaqAnswer(Long id, String updatedContent);

    void deleteFaqById(Long id);
}
