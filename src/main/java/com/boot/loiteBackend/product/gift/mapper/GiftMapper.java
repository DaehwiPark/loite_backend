package com.boot.loiteBackend.product.gift.mapper;

import com.boot.loiteBackend.product.gift.dto.GiftRequestDto;
import com.boot.loiteBackend.product.gift.dto.GiftResponseDto;
import com.boot.loiteBackend.product.gift.entity.GiftEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiftMapper {
    private final ModelMapper modelMapper;

    public GiftEntity toEntity(GiftRequestDto dto) {
        return modelMapper.map(dto, GiftEntity.class);
    }

    public GiftResponseDto toResponseDto(GiftEntity entity) {
        return modelMapper.map(entity, GiftResponseDto.class);
    }
}