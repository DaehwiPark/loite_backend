package com.boot.loiteBackend.web.support.counsel.service;

import com.boot.loiteBackend.admin.support.counsel.enums.AdminCounselStatus;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.support.counsel.dto.SupportCounselRequestDto;
import com.boot.loiteBackend.web.support.counsel.dto.SupportCounselResponseDto;
import com.boot.loiteBackend.domain.support.counsel.entity.SupportCounselEntity;
import com.boot.loiteBackend.web.support.counsel.error.SupportCounselErrorCode;
import com.boot.loiteBackend.web.support.counsel.repository.SupportCounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportCounselServiceImpl implements SupportCounselService {

    private final SupportCounselRepository counselRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long createCounsel(Long userId, SupportCounselRequestDto dto) {
        SupportCounselEntity entity = SupportCounselEntity.builder()
                .counselUserId(userId)
                .counselTitle(dto.getCounselTitle())
                .counselContent(dto.getCounselContent())
                .counselEmail(dto.getCounselEmail())
                .adminCounselStatus(AdminCounselStatus.WAITING)
                .counselPrivateYn(dto.getCounselPrivateYn())
                .counselPrivatePassword(dto.getCounselPrivatePassword() != null ? passwordEncoder.encode(dto.getCounselPrivatePassword()) : null)
                .build();
        return counselRepository.save(entity).getCounselId();
    }

    @Override
    public List<SupportCounselResponseDto> getMyCounsels(Long userId) {
        return counselRepository.findAllByCounselUserIdAndDeleteYn(userId, "N").stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupportCounselResponseDto getCounselDetail(Long counselId, Long userId, String inputPassword) {
        SupportCounselEntity entity = counselRepository.findById(counselId)
                .orElseThrow(() -> new CustomException(SupportCounselErrorCode.NOT_FOUND));
        if (!entity.getCounselUserId().equals(userId)) {
            throw new CustomException(SupportCounselErrorCode.ACCESS_DENIED);
        }
        if ("Y".equals(entity.getCounselPrivateYn()) && !passwordEncoder.matches(inputPassword, entity.getCounselPrivatePassword())) {
            throw new CustomException(SupportCounselErrorCode.INVALID_PASSWORD);
        }
        return toDto(entity);
    }

    @Override
    public boolean verifyPassword(Long counselId, String inputPassword) {
        SupportCounselEntity entity = counselRepository.findById(counselId)
                .orElseThrow(() -> new CustomException(SupportCounselErrorCode.NOT_FOUND));
        return passwordEncoder.matches(inputPassword, entity.getCounselPrivatePassword());
    }

    private SupportCounselResponseDto toDto(SupportCounselEntity e) {
        return SupportCounselResponseDto.builder()
                .counselId(e.getCounselId())
                .counselTitle(e.getCounselTitle())
                .counselContent(e.getCounselContent())
                .counselEmail(e.getCounselEmail())
                .counselStatus(e.getAdminCounselStatus().name())
                .counselReplyContent(e.getCounselReplyContent())
                .createdAt(e.getCreatedAt())
                .repliedAt(e.getCounselRepliedAt())
                .build();
    }
}