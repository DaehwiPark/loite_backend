package com.boot.loiteBackend.admin.support.counsel.service;

import com.boot.loiteBackend.admin.support.counsel.dto.AdminSupportCounselDto;
import com.boot.loiteBackend.admin.support.counsel.dto.AdminSupportCounselReplyDto;
import com.boot.loiteBackend.admin.support.counsel.entity.AdminSupportCounselEntity;
import com.boot.loiteBackend.admin.support.counsel.enums.AdminCounselStatus;
import com.boot.loiteBackend.admin.support.counsel.error.AdminCounselErrorCode;
import com.boot.loiteBackend.admin.support.counsel.mapper.AdminSupportCounselMapper;
import com.boot.loiteBackend.admin.support.counsel.repository.AdminSupportCounselRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.admin.support.counsel.spec.AdminSupportCounselSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminSupportCounselServiceImpl implements AdminSupportCounselService {

    private final AdminSupportCounselRepository adminSupportCounselRepository;
    private final AdminSupportCounselMapper adminSupportCounselMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminSupportCounselDto> getPagedCounsel(String keyword, Pageable pageable) {
        Specification<AdminSupportCounselEntity> spec = AdminSupportCounselSpecification.isNotDeleted();

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(AdminSupportCounselSpecification.containsKeyword(keyword));
        }

        Page<AdminSupportCounselEntity> page = adminSupportCounselRepository.findAll(spec, pageable);
        return page.map(adminSupportCounselMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminSupportCounselDto> getUnansweredPagedCounsel(String keyword, Pageable pageable) {
        Specification<AdminSupportCounselEntity> spec = AdminSupportCounselSpecification.isNotDeleted()
                .and(AdminSupportCounselSpecification.isUnanswered());

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(AdminSupportCounselSpecification.containsKeyword(keyword));
        }

        Page<AdminSupportCounselEntity> page = adminSupportCounselRepository.findAll(spec, pageable);
        return page.map(adminSupportCounselMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSupportCounselDto getCounselById(Long id) {
        return adminSupportCounselMapper.toDto(getEntityOrThrow(id));
    }

    @Override
    @Transactional
    public AdminSupportCounselDto addReply(Long id, AdminSupportCounselReplyDto replyDto) {
        AdminSupportCounselEntity entity = getEntityOrThrow(id);
        if (entity.getCounselReplyContent() != null) {
            throw new CustomException(AdminCounselErrorCode.ALREADY_ANSWERED);
        }
        applyReply(entity, replyDto);
        return adminSupportCounselMapper.toDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportCounselDto updateReply(Long id, AdminSupportCounselReplyDto replyDto) {
        AdminSupportCounselEntity entity = getEntityOrThrow(id);
        applyReply(entity, replyDto);
        return adminSupportCounselMapper.toDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportCounselDto deleteReply(Long id) {
        AdminSupportCounselEntity entity = getEntityOrThrow(id);

        entity.setCounselReplyContent(null);
        entity.setCounselRepliedAt(null);
        entity.setCounselRepliedBy(null);

        entity.setAdminCounselStatus(AdminCounselStatus.WAITING);

        return adminSupportCounselMapper.toDto(entity);
    }


    @Override
    @Transactional
    public void softDeleteCounsel(Long id) {
        AdminSupportCounselEntity entity = getEntityOrThrow(id);
        entity.setDeleteYn("Y");
    }

    @Override
    @Transactional
    public AdminSupportCounselDto updateStatus(Long id, String status) {
        AdminSupportCounselEntity entity = getEntityOrThrow(id);
        entity.setAdminCounselStatus(AdminCounselStatus.from(status));
        return adminSupportCounselMapper.toDto(entity);
    }

    private AdminSupportCounselEntity getEntityOrThrow(Long id) {
        return adminSupportCounselRepository.findByCounselIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new CustomException(AdminCounselErrorCode.NOT_FOUND));
    }

    private void applyReply(AdminSupportCounselEntity entity, AdminSupportCounselReplyDto replyDto) {
        String content = replyDto.getReplyContent();
        if (content == null || content.trim().isEmpty()) {
            throw new CustomException(AdminCounselErrorCode.REPLY_CONTENT_EMPTY);
        }
        entity.setCounselReplyContent(replyDto.getReplyContent());
        entity.setCounselRepliedBy(replyDto.getRepliedBy());
        entity.setCounselRepliedAt(LocalDateTime.now());
        entity.setAdminCounselStatus(AdminCounselStatus.COMPLETE);
    }
}
