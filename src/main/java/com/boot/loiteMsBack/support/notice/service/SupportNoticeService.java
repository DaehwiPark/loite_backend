package com.boot.loiteMsBack.support.notice.service;

import com.boot.loiteMsBack.support.notice.dto.SupportNoticeDto;
import com.boot.loiteMsBack.support.notice.dto.SupportNoticeRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupportNoticeService {
    SupportNoticeDto createNotice(SupportNoticeRequestDto requestDto);
    SupportNoticeDto updateNotice(Long id, SupportNoticeRequestDto requestDto);
    void deleteNotice(Long id);
    SupportNoticeDto getNoticeById(Long id);
    Page<SupportNoticeDto> getPagedNotices(String Keyword, Pageable pageable);
}
