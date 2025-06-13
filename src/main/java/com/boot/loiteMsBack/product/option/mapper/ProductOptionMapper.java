package com.boot.loiteMsBack.product.option.mapper;

import com.boot.loiteMsBack.product.option.dto.ProductOptionRequestDto;
import com.boot.loiteMsBack.product.option.entity.ProductOptionEntity;
import com.boot.loiteMsBack.product.option.repository.ProductOptionRepository;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
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
