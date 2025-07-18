package com.boot.loiteBackend.admin.product.product.mapper;

import com.boot.loiteBackend.admin.product.product.dto.AdminProductRequestDto;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductMapper {

    private final ModelMapper modelMapper;

    public AdminProductRequestDto toDto(AdminProductEntity entity){
        return modelMapper.map(entity, AdminProductRequestDto.class);
    };

    public AdminProductEntity toEntity(AdminProductRequestDto dto){
        return modelMapper.map(dto, AdminProductEntity.class);
    }

}