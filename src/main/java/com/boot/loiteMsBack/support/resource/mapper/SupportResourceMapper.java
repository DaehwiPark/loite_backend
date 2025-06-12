package com.boot.loiteMsBack.support.resource.mapper;

import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.entity.SupportResourceEntity;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportResourceMapper {

    private final ModelMapper modelMapper;

    public SupportResourceDto toDto(SupportResourceEntity entity) {
        if (entity == null) return null;

        SupportResourceDto dto = modelMapper.map(entity, SupportResourceDto.class);

        ProductEntity product = entity.getProduct();
        if (product != null) {
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setProductModelName(product.getProductModelName());
        }
        return dto;
    }

    public SupportResourceEntity toEntity(SupportResourceDto dto) {
        return modelMapper.map(dto, SupportResourceEntity.class);
    }
}
