package com.boot.loiteBackend.admin.product.gift.service;

import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftRequestDto;
import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftResponseDto;
import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.gift.mapper.AdminGiftMapper;
import com.boot.loiteBackend.admin.product.gift.repository.AdminGiftRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminGiftServiceImpl implements AdminGiftService {

    private final AdminGiftRepository adminGiftRepository;
    private final AdminGiftMapper adminGiftMapper;
    private final FileService fileService;

    @Override
    public Long saveGift(AdminGiftRequestDto dto, MultipartFile imageFile) {
        AdminGiftEntity entity = adminGiftMapper.toEntity(dto);

        if (entity.getDeleteYn() == null) {
            entity.setDeleteYn("N");
        }
        if (entity.getActiveYn() == null) {
            entity.setActiveYn("Y");
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            FileUploadResult result = fileService.save(imageFile, "gift");
            if (result != null) {
                entity.setGiftImageUrl(result.getUrlPath());
            }
        }

        AdminGiftEntity saved = adminGiftRepository.save(entity);
        return saved.getGiftId();
    }

    @Override
    public void updateGift(Long giftId, AdminGiftRequestDto dto) {
        AdminGiftEntity gift = adminGiftRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사은품이 존재하지 않습니다."));

        gift.setGiftName(dto.getGiftName());
        gift.setGiftStock(dto.getGiftStock());
        gift.setSoldOutYn(dto.getGiftStock() <= 0 ? "Y" : "N");
        gift.setActiveYn(dto.getActiveYn() != null ? dto.getActiveYn() : gift.getActiveYn());
        gift.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public List<AdminGiftResponseDto> getAllGifts() {
        List<AdminGiftEntity> giftList = adminGiftRepository.findByDeleteYn("N");  // 소프트 삭제 제외
        return giftList.stream()
                .map(adminGiftMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminGiftResponseDto getGift(Long giftId) {
        AdminGiftEntity gift = adminGiftRepository.findById(giftId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사은품이 존재하지 않습니다."));

        return AdminGiftResponseDto.builder()
                .giftId(gift.getGiftId())
                .giftName(gift.getGiftName())
                .giftStock(gift.getGiftStock())
                .giftImageUrl(gift.getGiftImageUrl())
                .activeYn(gift.getActiveYn())
                .deleteYn(gift.getDeleteYn())
                .build();
    }

    @Override
    public void deleteGift(Long giftId) {
        AdminGiftEntity entity = adminGiftRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사은품이 존재하지 않습니다."));
        entity.setDeleteYn("Y");
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
