package com.boot.loiteBackend.admin.support.notice.service;

import com.boot.loiteBackend.admin.support.notice.dto.AdminSupportNoticeDto;
import com.boot.loiteBackend.admin.support.notice.dto.AdminSupportNoticeRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSupportNoticeService {
    AdminSupportNoticeDto createNotice(AdminSupportNoticeRequestDto requestDto);
    AdminSupportNoticeDto updateNotice(Long id, AdminSupportNoticeRequestDto requestDto);
    void deleteNotice(Long id);
    AdminSupportNoticeDto getNoticeById(Long id);
    Page<AdminSupportNoticeDto> getPagedNotices(String Keyword, Pageable pageable);
}
