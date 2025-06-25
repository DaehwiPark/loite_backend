package com.boot.loiteBackend.support.counsel.service;

import com.boot.loiteBackend.support.counsel.dto.SupportCounselDto;
import com.boot.loiteBackend.support.counsel.dto.SupportCounselReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupportCounselService {
    Page<SupportCounselDto> getPagedCounsel(String keyword, Pageable pageable);

    SupportCounselDto getCounselById(Long id);

    SupportCounselDto addReply(Long id, SupportCounselReplyDto replyDto);

    SupportCounselDto updateReply(Long id, SupportCounselReplyDto replyDto);

    SupportCounselDto deleteReply(Long id);

    void softDeleteCounsel(Long id);

    SupportCounselDto updateStatus(Long id, String status);

    Page<SupportCounselDto> getUnansweredPagedCounsel(String keyword, Pageable pageable);
}
