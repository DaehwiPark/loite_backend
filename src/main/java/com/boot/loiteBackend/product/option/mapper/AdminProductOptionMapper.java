package com.boot.loiteBackend.product.option.mapper;

import com.boot.loiteBackend.product.option.dto.AdminProductOptionRequestDto;
import com.boot.loiteBackend.product.option.entity.ProductOptionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductOptionMapper {
    private final ModelMapper modelMapper;

    public AdminProductOptionRequestDto toRequestDto(ProductOptionEntity entity){
        return modelMapper.map(entity, AdminProductOptionRequestDto.class);
    }

    public ProductOptionEntity toEntity(AdminProductOptionRequestDto dto){
        return modelMapper.map(dto, ProductOptionEntity.class);
    }
}
