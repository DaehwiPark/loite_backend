package com.boot.loiteBackend.admin.product.option.mapper;

import com.boot.loiteBackend.admin.product.option.dto.ProductOptionRequestDto;
import com.boot.loiteBackend.admin.product.option.entity.ProductOptionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductOptionMapper {
    private final ModelMapper modelMapper;

    public ProductOptionRequestDto toRequestDto(ProductOptionEntity entity){
        return modelMapper.map(entity, ProductOptionRequestDto.class);
    }

    public ProductOptionEntity toEntity(ProductOptionRequestDto dto){
        return modelMapper.map(dto, ProductOptionEntity.class);
    }
}
