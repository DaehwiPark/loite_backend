package com.boot.loiteBackend.admin.support.notice.service;

import com.boot.loiteBackend.admin.support.notice.dto.AdminSupportNoticeDto;
import com.boot.loiteBackend.admin.support.notice.dto.AdminSupportNoticeRequestDto;
import com.boot.loiteBackend.admin.support.notice.entity.AdminSupportNoticeEntity;
import com.boot.loiteBackend.admin.support.notice.error.AdminSupportNoticeErrorCode;
import com.boot.loiteBackend.admin.support.notice.repository.AdminSupportNoticeRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSupportNoticeServiceImpl implements AdminSupportNoticeService {

    private final AdminSupportNoticeRepository adminSupportNoticeRepository;

    @Transactional
    public AdminSupportNoticeDto createNotice(AdminSupportNoticeRequestDto requestDto) {
        AdminSupportNoticeEntity entity = AdminSupportNoticeEntity.builder()
                .noticeTitle(requestDto.getNoticeTitle())
                .noticeContent(requestDto.getNoticeContent())
                .noticeViewCount(0)
                .displayYn("Y")
                .pinnedYn(requestDto.getPinnedYn())
                .build();
        adminSupportNoticeRepository.save(entity);
        return new AdminSupportNoticeDto(entity);
    }

    @Transactional
    public AdminSupportNoticeDto updateNotice(Long id, AdminSupportNoticeRequestDto requestDto) {
        AdminSupportNoticeEntity entity = adminSupportNoticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminSupportNoticeErrorCode.NOT_FOUND));

        entity.setNoticeTitle(requestDto.getNoticeTitle());
        entity.setNoticeContent(requestDto.getNoticeContent());
        entity.setPinnedYn(requestDto.getPinnedYn());
        return new AdminSupportNoticeDto(entity);
    }

    @Transactional
    public void deleteNotice(Long id) {
        AdminSupportNoticeEntity entity = adminSupportNoticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminSupportNoticeErrorCode.NOT_FOUND));
        entity.setDisplayYn("N");
        adminSupportNoticeRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Page<AdminSupportNoticeDto> getPagedNotices(String keyword, Pageable pageable) {
        return adminSupportNoticeRepository.findAllByKeyword(keyword, pageable).map(AdminSupportNoticeDto::new);
    }

    @Transactional(readOnly = true)
    public AdminSupportNoticeDto getNoticeById(Long id) {
        AdminSupportNoticeEntity entity = adminSupportNoticeRepository.findById(id)
                .filter(n -> "Y".equals(n.getDisplayYn()))
                .orElseThrow(() -> new CustomException(AdminSupportNoticeErrorCode.NOT_FOUND));
        return new AdminSupportNoticeDto(entity);
    }
}
