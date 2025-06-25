package com.boot.loiteBackend.admin.product.brand.Mapper;

import com.boot.loiteBackend.admin.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteBackend.admin.product.brand.dto.ProductBrandResponseDto;
import com.boot.loiteBackend.admin.product.brand.entity.ProductBrandEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductBrandMapper {

    private final ModelMapper modelMapper;

    public ProductBrandRequestDto toRequestDto(ProductBrandEntity entity){
        return modelMapper.map(entity, ProductBrandRequestDto.class);
    }

    public ProductBrandResponseDto toResponseDto(ProductBrandEntity entity){
        return modelMapper.map(entity, ProductBrandResponseDto.class);
    }

    public ProductBrandEntity toEntity(ProductBrandRequestDto dto){
        return modelMapper.map(dto, ProductBrandEntity.class);
    }
}
