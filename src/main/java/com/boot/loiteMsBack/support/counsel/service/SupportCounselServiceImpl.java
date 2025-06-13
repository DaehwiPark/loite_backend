package com.boot.loiteMsBack.support.counsel.service;

import com.boot.loiteMsBack.support.counsel.dto.SupportCounselDto;
import com.boot.loiteMsBack.support.counsel.dto.SupportCounselReplyDto;
import com.boot.loiteMsBack.support.counsel.entity.SupportCounselEntity;
import com.boot.loiteMsBack.support.counsel.enums.CounselStatus;
import com.boot.loiteMsBack.support.counsel.error.CounselErrorCode;
import com.boot.loiteMsBack.support.counsel.mapper.SupportCounselMapper;
import com.boot.loiteMsBack.support.counsel.repository.SupportCounselRepository;
import com.boot.loiteMsBack.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportCounselServiceImpl implements SupportCounselService {

    private final SupportCounselRepository counselRepository;
    private final SupportCounselMapper supportCounselMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SupportCounselDto> getAllCounsel() {
        return counselRepository.findByDeleteYn("N").stream()
                .map(supportCounselMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupportCounselDto getCounselById(Long id) {
        return supportCounselMapper.toDto(getEntityOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportCounselDto> getUnansweredCounsel() {
        return counselRepository.findByDeleteYnAndCounselReplyContentIsNull("N").stream()
                .map(supportCounselMapper::toDto)
                .collect(Collectors.toList());
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
        return counselRepository.findByCounselIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new CustomException(CounselErrorCode.NOT_FOUND));
    }

    private void applyReply(SupportCounselEntity entity, SupportCounselReplyDto replyDto) {
        entity.setCounselReplyContent(replyDto.getReplyContent());
        entity.setCounselRepliedBy(replyDto.getRepliedBy());
        entity.setCounselRepliedAt(LocalDateTime.now());
    }
}
