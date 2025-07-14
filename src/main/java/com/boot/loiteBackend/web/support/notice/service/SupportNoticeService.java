package com.boot.loiteBackend.web.support.notice.service;

import com.boot.loiteBackend.web.support.notice.dto.SupportNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupportNoticeService {

    Page<SupportNoticeDto> getPagedNotices(String Keyword, Pageable pageable);

    void insertViewCount(Long id);

}
