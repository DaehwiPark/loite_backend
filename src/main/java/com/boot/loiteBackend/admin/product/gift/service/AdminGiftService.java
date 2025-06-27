package com.boot.loiteBackend.admin.product.gift.service;

import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftRequestDto;
import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftResponseDto;

import java.util.List;

public interface AdminGiftService {
    Long saveGift(AdminGiftRequestDto dto);
    List<AdminGiftResponseDto> getAllGifts();
    void updateGift(Long giftId, AdminGiftRequestDto dto);
    void deleteGift(Long giftId);
}
