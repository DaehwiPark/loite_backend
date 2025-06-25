package com.boot.loiteBackend.product.gift.service;

import com.boot.loiteBackend.product.gift.dto.GiftRequestDto;
import com.boot.loiteBackend.product.gift.dto.GiftResponseDto;
import com.boot.loiteBackend.product.gift.entity.GiftEntity;
import com.boot.loiteBackend.product.gift.mapper.GiftMapper;
import com.boot.loiteBackend.product.gift.repository.GiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GiftServiceImpl implements GiftService {

    private final GiftRepository giftRepository;
    private final GiftMapper giftMapper;

    @Override
    public Long saveGift(GiftRequestDto dto) {
        GiftEntity entity = giftMapper.toEntity(dto);

        // 기본값 처리
        if (entity.getDeleteYn() == null) {
            entity.setDeleteYn("N");
        }
        if (entity.getActiveYn() == null) {
            entity.setActiveYn("Y");
        }

        GiftEntity saved = giftRepository.save(entity);
        return saved.getGiftId();
    }

    @Override
    public void updateGift(Long giftId, GiftRequestDto dto) {
        GiftEntity gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사은품이 존재하지 않습니다."));

        gift.setGiftName(dto.getGiftName());
        gift.setGiftStock(dto.getGiftStock());
        gift.setActiveYn(dto.getActiveYn() != null ? dto.getActiveYn() : gift.getActiveYn());
        gift.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public List<GiftResponseDto> getAllGifts() {
        List<GiftEntity> giftList = giftRepository.findByDeleteYn("N");  // 소프트 삭제 제외
        return giftList.stream()
                .map(giftMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteGift(Long giftId) {
        GiftEntity entity = giftRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사은품이 존재하지 않습니다."));
        entity.setDeleteYn("Y");
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
