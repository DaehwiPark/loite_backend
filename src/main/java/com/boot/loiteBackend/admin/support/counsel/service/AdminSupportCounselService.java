package com.boot.loiteBackend.admin.support.counsel.service;

import com.boot.loiteBackend.admin.support.counsel.dto.AdminSupportCounselDto;
import com.boot.loiteBackend.admin.support.counsel.dto.AdminSupportCounselReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSupportCounselService {
    Page<AdminSupportCounselDto> getPagedCounsel(String keyword, Pageable pageable);

    AdminSupportCounselDto getCounselById(Long id);

    AdminSupportCounselDto addReply(Long id, AdminSupportCounselReplyDto replyDto);

    AdminSupportCounselDto updateReply(Long id, AdminSupportCounselReplyDto replyDto);

    AdminSupportCounselDto deleteReply(Long id);

    void softDeleteCounsel(Long id);

    AdminSupportCounselDto updateStatus(Long id, String status);

    Page<AdminSupportCounselDto> getUnansweredPagedCounsel(String keyword, Pageable pageable);
}
