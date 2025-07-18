package com.boot.loiteBackend.admin.product.category.mapper;

import com.boot.loiteBackend.admin.product.category.dto.AdminProductCategoryRequestDto;
import com.boot.loiteBackend.admin.product.category.dto.AdminProductCategoryResponseDto;
import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductCategoryMapper {
    private final ModelMapper modelMapper;

    public AdminProductCategoryResponseDto toResponseDto(AdminProductCategoryEntity entity){
        return modelMapper.map(entity, AdminProductCategoryResponseDto.class);
    }

    public AdminProductCategoryEntity toEntity(AdminProductCategoryRequestDto dto){
        return modelMapper.map(dto, AdminProductCategoryEntity.class);
    }
}
