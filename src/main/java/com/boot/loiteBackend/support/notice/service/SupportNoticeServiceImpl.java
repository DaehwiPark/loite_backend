package com.boot.loiteBackend.support.notice.service;

import com.boot.loiteBackend.support.notice.dto.SupportNoticeDto;
import com.boot.loiteBackend.support.notice.dto.SupportNoticeRequestDto;
import com.boot.loiteBackend.support.notice.entity.SupportNoticeEntity;
import com.boot.loiteBackend.support.notice.error.SupportNoticeErrorCode;
import com.boot.loiteBackend.support.notice.repository.SupportNoticeRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupportNoticeServiceImpl implements SupportNoticeService {

    private final SupportNoticeRepository supportNoticeRepository;

    @Transactional
    public SupportNoticeDto createNotice(SupportNoticeRequestDto requestDto) {
        SupportNoticeEntity entity = SupportNoticeEntity.builder()
                .noticeTitle(requestDto.getNoticeTitle())
                .noticeContent(requestDto.getNoticeContent())
                .noticeViewCount(0)
                .displayYn("Y")
                .pinnedYn(requestDto.getPinnedYn())
                .build();
        supportNoticeRepository.save(entity);
        return new SupportNoticeDto(entity);
    }

    @Transactional
    public SupportNoticeDto updateNotice(Long id, SupportNoticeRequestDto requestDto) {
        SupportNoticeEntity entity = supportNoticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(SupportNoticeErrorCode.NOT_FOUND));

        entity.setNoticeTitle(requestDto.getNoticeTitle());
        entity.setNoticeContent(requestDto.getNoticeContent());
        entity.setPinnedYn(requestDto.getPinnedYn());
        return new SupportNoticeDto(entity);
    }

    @Transactional
    public void deleteNotice(Long id) {
        SupportNoticeEntity entity = supportNoticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(SupportNoticeErrorCode.NOT_FOUND));
        entity.setDisplayYn("N");
        supportNoticeRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Page<SupportNoticeDto> getPagedNotices(String keyword, Pageable pageable) {
        return supportNoticeRepository.findAllByKeyword(keyword, pageable).map(SupportNoticeDto::new);
    }

    @Transactional(readOnly = true)
    public SupportNoticeDto getNoticeById(Long id) {
        SupportNoticeEntity entity = supportNoticeRepository.findById(id)
                .filter(n -> "Y".equals(n.getDisplayYn()))
                .orElseThrow(() -> new CustomException(SupportNoticeErrorCode.NOT_FOUND));
        return new SupportNoticeDto(entity);
    }
}
