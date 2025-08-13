package com.boot.loiteBackend.admin.product.gift.service;

import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftRequestDto;
import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftResponseDto;
import com.boot.loiteBackend.admin.product.gift.dto.AdminGiftUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminGiftService {
    Long saveGift(AdminGiftRequestDto dto, MultipartFile imageFile);

    List<AdminGiftResponseDto> getAllGifts();

    AdminGiftResponseDto getGift(Long giftId);

    void updateGift(Long giftId, AdminGiftUpdateRequestDto dto);

    void deleteGift(Long giftId);
}
