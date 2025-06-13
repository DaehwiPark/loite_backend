package com.boot.loiteMsBack.notice.service;

import com.boot.loiteMsBack.notice.dto.NoticeDto;
import com.boot.loiteMsBack.notice.dto.NoticeRequestDto;

import java.util.List;

public interface NoticeService {
    NoticeDto createNotice(NoticeRequestDto requestDto);
    NoticeDto updateNotice(Long id, NoticeRequestDto requestDto);
    void deleteNotice(Long id);
    NoticeDto getNoticeById(Long id);
    List<NoticeDto> getAllNotices();
}
