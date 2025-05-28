package com.boot.loiteMsBack.support.counsel.service;

import com.boot.loiteMsBack.support.counsel.dto.SupportCounselDto;
import com.boot.loiteMsBack.support.counsel.dto.SupportCounselReplyDto;
import com.boot.loiteMsBack.support.counsel.dto.SupportCounselStatusUpdateDto;

import java.util.List;

public interface SupportCounselService {

    List<SupportCounselDto> getAllCounsel();

    SupportCounselDto getCounselById(Long id);

    List<SupportCounselDto> getUnansweredCounsel();

    SupportCounselDto addReply(Long id, SupportCounselReplyDto replyDto);

    SupportCounselDto updateReply(Long id, SupportCounselReplyDto replyDto);

    void softDeleteCounsel(Long id);

    SupportCounselDto updateStatus(Long id, String status);
}
