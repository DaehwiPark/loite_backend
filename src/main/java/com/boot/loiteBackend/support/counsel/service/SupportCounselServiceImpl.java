package com.boot.loiteBackend.support.counsel.service;

import com.boot.loiteBackend.support.counsel.dto.SupportCounselDto;
import com.boot.loiteBackend.support.counsel.dto.SupportCounselReplyDto;
import com.boot.loiteBackend.support.counsel.entity.SupportCounselEntity;
import com.boot.loiteBackend.support.counsel.enums.CounselStatus;
import com.boot.loiteBackend.support.counsel.error.CounselErrorCode;
import com.boot.loiteBackend.support.counsel.mapper.SupportCounselMapper;
import com.boot.loiteBackend.support.counsel.repository.SupportCounselRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.support.counsel.spec.SupportCounselSpecification;
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
public class SupportCounselServiceImpl implements SupportCounselService {

    private final SupportCounselRepository supportCounselRepository;
    private final SupportCounselMapper supportCounselMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SupportCounselDto> getPagedCounsel(String keyword, Pageable pageable) {
        Specification<SupportCounselEntity> spec = SupportCounselSpecification.isNotDeleted();

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(SupportCounselSpecification.containsKeyword(keyword));
        }

        Page<SupportCounselEntity> page = supportCounselRepository.findAll(spec, pageable);
        return page.map(supportCounselMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupportCounselDto> getUnansweredPagedCounsel(String keyword, Pageable pageable) {
        Specification<SupportCounselEntity> spec = SupportCounselSpecification.isNotDeleted()
                .and(SupportCounselSpecification.isUnanswered());

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(SupportCounselSpecification.containsKeyword(keyword));
        }

        Page<SupportCounselEntity> page = supportCounselRepository.findAll(spec, pageable);
        return page.map(supportCounselMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportCounselDto getCounselById(Long id) {
        return supportCounselMapper.toDto(getEntityOrThrow(id));
    }

    @Override
    @Transactional
    public SupportCounselDto addReply(Long id, SupportCounselReplyDto replyDto) {
        SupportCounselEntity entity = getEntityOrThrow(id);
        if (entity.getCounselReplyContent() != null) {
            throw new CustomException(CounselErrorCode.ALREADY_ANSWERED);
        }
        applyReply(entity, replyDto);
        return supportCounselMapper.toDto(entity);
    }

    @Override
    @Transactional
    public SupportCounselDto updateReply(Long id, SupportCounselReplyDto replyDto) {
        SupportCounselEntity entity = getEntityOrThrow(id);
        applyReply(entity, replyDto);
        return supportCounselMapper.toDto(entity);
    }

    @Override
    @Transactional
    public SupportCounselDto deleteReply(Long id) {
        SupportCounselEntity entity = getEntityOrThrow(id);

        entity.setCounselReplyContent(null);
        entity.setCounselRepliedAt(null);
        entity.setCounselRepliedBy(null);

        entity.setCounselStatus(CounselStatus.WAITING);

        return supportCounselMapper.toDto(entity);
    }


    @Override
    @Transactional
    public void softDeleteCounsel(Long id) {
        SupportCounselEntity entity = getEntityOrThrow(id);
        entity.setDeleteYn("Y");
    }

    @Override
    @Transactional
    public SupportCounselDto updateStatus(Long id, String status) {
        SupportCounselEntity entity = getEntityOrThrow(id);
        entity.setCounselStatus(CounselStatus.from(status));
        return supportCounselMapper.toDto(entity);
    }

    private SupportCounselEntity getEntityOrThrow(Long id) {
        return supportCounselRepository.findByCounselIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new CustomException(CounselErrorCode.NOT_FOUND));
    }

    private void applyReply(SupportCounselEntity entity, SupportCounselReplyDto replyDto) {
        String content = replyDto.getReplyContent();
        if (content == null || content.trim().isEmpty()) {
            throw new CustomException(CounselErrorCode.REPLY_CONTENT_EMPTY);
        }
        entity.setCounselReplyContent(replyDto.getReplyContent());
        entity.setCounselRepliedBy(replyDto.getRepliedBy());
        entity.setCounselRepliedAt(LocalDateTime.now());
        entity.setCounselStatus(CounselStatus.COMPLETE);
    }
}
