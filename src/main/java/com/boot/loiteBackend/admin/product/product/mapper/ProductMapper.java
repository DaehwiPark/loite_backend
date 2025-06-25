package com.boot.loiteBackend.admin.product.product.mapper;

import com.boot.loiteBackend.admin.product.product.dto.ProductRequestDto;
import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductRequestDto toDto(ProductEntity entity){
        return modelMapper.map(entity, ProductRequestDto.class);
    };

    public ProductEntity toEntity(ProductRequestDto dto){
        return modelMapper.map(dto, ProductEntity.class);
    }

}