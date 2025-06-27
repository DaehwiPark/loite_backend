package com.boot.loiteBackend.admin.product.gift.service;

import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftRequestDto;
import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftResponseDto;
import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.gift.mapper.AdminGiftMapper;
import com.boot.loiteBackend.admin.product.gift.repository.AdminGiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminGiftServiceImpl implements AdminGiftService {

    private final AdminGiftRepository adminGiftRepository;
    private final AdminGiftMapper adminGiftMapper;

    @Override
    public Long saveGift(AdminGiftRequestDto dto) {
        AdminGiftEntity entity = adminGiftMapper.toEntity(dto);

        // 기본값 처리
        if (entity.getDeleteYn() == null) {
            entity.setDeleteYn("N");
        }
        if (entity.getActiveYn() == null) {
            entity.setActiveYn("Y");
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
    public void deleteGift(Long giftId) {
        AdminGiftEntity entity = adminGiftRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사은품이 존재하지 않습니다."));
        entity.setDeleteYn("Y");
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
