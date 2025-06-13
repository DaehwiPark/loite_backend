package com.boot.loiteMsBack.notice.service;

import com.boot.loiteMsBack.notice.dto.NoticeDto;
import com.boot.loiteMsBack.notice.dto.NoticeRequestDto;
import com.boot.loiteMsBack.notice.entity.NoticeEntity;
import com.boot.loiteMsBack.notice.error.NoticeErrorCode;
import com.boot.loiteMsBack.notice.repository.NoticeRepository;
import com.boot.loiteMsBack.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeDto createNotice(NoticeRequestDto requestDto) {
        NoticeEntity entity = NoticeEntity.builder()
                .noticeTitle(requestDto.getNoticeTitle())
                .noticeContent(requestDto.getNoticeContent())
                .noticeViewCount(0)
                .deleteYn("N")
                .build();
        noticeRepository.save(entity);
        return new NoticeDto(entity);
    }

    @Transactional
    public NoticeDto updateNotice(Long id, NoticeRequestDto requestDto) {
        NoticeEntity entity = noticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(NoticeErrorCode.NOT_FOUND));

        entity.setNoticeTitle(requestDto.getNoticeTitle());
        entity.setNoticeContent(requestDto.getNoticeContent());
        return new NoticeDto(entity);
    }

    @Transactional
    public void deleteNotice(Long id) {
        NoticeEntity entity = noticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(NoticeErrorCode.NOT_FOUND));
        entity.setDeleteYn("Y");
        noticeRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<NoticeDto> getAllNotices() {
        return noticeRepository.findAll().stream()
                .filter(n -> "N".equals(n.getDeleteYn()))
                .map(NoticeDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoticeDto getNoticeById(Long id) {
        NoticeEntity entity = noticeRepository.findById(id)
                .filter(n -> "N".equals(n.getDeleteYn()))
                .orElseThrow(() -> new CustomException(NoticeErrorCode.NOT_FOUND));
        return new NoticeDto(entity);
    }
}
