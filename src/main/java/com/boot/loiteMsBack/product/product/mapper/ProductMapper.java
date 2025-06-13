package com.boot.loiteMsBack.product.product.mapper;

import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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