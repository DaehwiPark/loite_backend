package com.boot.loiteBackend.admin.product.category.mapper;

import com.boot.loiteBackend.admin.product.category.dto.ProductCategoryRequestDto;
import com.boot.loiteBackend.admin.product.category.dto.ProductCategoryResponseDto;
import com.boot.loiteBackend.admin.product.category.entity.ProductCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCategoryMapper {
    private final ModelMapper modelMapper;

    public ProductCategoryResponseDto toResponseDto(ProductCategoryEntity entity){
        return modelMapper.map(entity, ProductCategoryResponseDto.class);
    }

    public ProductCategoryEntity toEntity(ProductCategoryRequestDto dto){
        return modelMapper.map(dto, ProductCategoryEntity.class);
    }
}
