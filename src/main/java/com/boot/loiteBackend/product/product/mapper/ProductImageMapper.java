package com.boot.loiteBackend.product.product.mapper;

import com.boot.loiteBackend.product.product.dto.ProductImageRequestDto;
import com.boot.loiteBackend.product.product.entity.ProductImageEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {
    private final ModelMapper modelMapper;

    public ProductImageRequestDto toRequestDto(ProductImageEntity entity){
        return modelMapper.map(entity, ProductImageRequestDto.class);
    }

    public ProductImageEntity toEntity(ProductImageRequestDto dto){
        return  modelMapper.map(dto, ProductImageEntity.class);
    }
}
