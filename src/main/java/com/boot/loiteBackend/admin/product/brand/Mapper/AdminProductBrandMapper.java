package com.boot.loiteBackend.admin.product.brand.Mapper;

import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandRequestDto;
import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandResponseDto;
import com.boot.loiteBackend.admin.product.brand.entity.AdminProductBrandEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductBrandMapper {

    private final ModelMapper modelMapper;

    public AdminProductBrandRequestDto toRequestDto(AdminProductBrandEntity entity){
        return modelMapper.map(entity, AdminProductBrandRequestDto.class);
    }

    public AdminProductBrandResponseDto toResponseDto(AdminProductBrandEntity entity){
        return modelMapper.map(entity, AdminProductBrandResponseDto.class);
    }

    public AdminProductBrandEntity toEntity(AdminProductBrandRequestDto dto){
        return modelMapper.map(dto, AdminProductBrandEntity.class);
    }
}
