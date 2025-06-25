package com.boot.loiteMsBack.product.gift.service;

import com.boot.loiteMsBack.product.gift.dto.GiftRequestDto;
import com.boot.loiteMsBack.product.gift.dto.GiftResponseDto;

import java.util.List;

public interface GiftService {
    Long saveGift(GiftRequestDto dto);
    List<GiftResponseDto> getAllGifts();
    void updateGift(Long giftId, GiftRequestDto dto);
    void deleteGift(Long giftId);
}
