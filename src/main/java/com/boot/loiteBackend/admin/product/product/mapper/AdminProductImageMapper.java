package com.boot.loiteBackend.admin.product.product.mapper;

import com.boot.loiteBackend.admin.product.product.dto.AdminProductImageRequestDto;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductImageMapper {
    private final ModelMapper modelMapper;

    public AdminProductImageRequestDto toRequestDto(AdminProductImageEntity entity){
        return modelMapper.map(entity, AdminProductImageRequestDto.class);
    }

    public AdminProductImageEntity toEntity(AdminProductImageRequestDto dto){
        return  modelMapper.map(dto, AdminProductImageEntity.class);
    }
}
