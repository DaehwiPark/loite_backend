package com.boot.loiteBackend.product.gift.mapper;

import com.boot.loiteBackend.product.gift.dto.AdminGiftRequestDto;
import com.boot.loiteBackend.product.gift.dto.AdminGiftResponseDto;
import com.boot.loiteBackend.product.gift.entity.AdminGiftEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminGiftMapper {
    private final ModelMapper modelMapper;

    public AdminGiftEntity toEntity(AdminGiftRequestDto dto) {
        return modelMapper.map(dto, AdminGiftEntity.class);
    }

    public AdminGiftResponseDto toResponseDto(AdminGiftEntity entity) {
        return modelMapper.map(entity, AdminGiftResponseDto.class);
    }
}