package com.boot.loiteBackend.product.option.mapper;

import com.boot.loiteBackend.product.option.dto.AdminProductOptionRequestDto;
import com.boot.loiteBackend.product.option.entity.AdminProductOptionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductOptionMapper {
    private final ModelMapper modelMapper;

    public AdminProductOptionRequestDto toRequestDto(AdminProductOptionEntity entity){
        return modelMapper.map(entity, AdminProductOptionRequestDto.class);
    }

    public AdminProductOptionEntity toEntity(AdminProductOptionRequestDto dto){
        return modelMapper.map(dto, AdminProductOptionEntity.class);
    }
}
