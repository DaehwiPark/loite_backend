package com.boot.loiteBackend.web.support.notice.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.support.notice.dto.SupportNoticeDto;
import com.boot.loiteBackend.web.support.notice.entity.SupportNoticeEntity;
import com.boot.loiteBackend.web.support.notice.error.SupportNoticeErrorCode;
import com.boot.loiteBackend.web.support.notice.repository.SupportNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupportNoticeServiceImpl implements SupportNoticeService {

    private final SupportNoticeRepository supportNoticeRepository;

    @Transactional(readOnly = true)
    public Page<SupportNoticeDto> getPagedNotices(String keyword, Pageable pageable) {
        return supportNoticeRepository.findAllByKeyword(keyword, pageable)
                .map(SupportNoticeDto::new);
    }

    @Transactional
    public void insertViewCount(Long id) {
        SupportNoticeEntity entity = supportNoticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(SupportNoticeErrorCode.NOT_FOUND));
        entity.insertViewCount();
    }
}
