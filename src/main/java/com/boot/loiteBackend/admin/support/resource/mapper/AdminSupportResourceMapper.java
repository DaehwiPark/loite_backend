package com.boot.loiteBackend.admin.support.resource.mapper;

import com.boot.loiteBackend.admin.support.resource.dto.AdminSupportResourceDto;
import com.boot.loiteBackend.admin.support.resource.entity.AdminSupportResourceEntity;
import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSupportResourceMapper {

    private final ModelMapper modelMapper;

    public AdminSupportResourceDto toDto(AdminSupportResourceEntity entity) {
        if (entity == null) return null;

        AdminSupportResourceDto dto = modelMapper.map(entity, AdminSupportResourceDto.class);

        ProductEntity product = entity.getProduct();
        if (product != null) {
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setProductModelName(product.getProductModelName());
        }
        return dto;
    }

    public AdminSupportResourceEntity toEntity(AdminSupportResourceDto dto) {
        return modelMapper.map(dto, AdminSupportResourceEntity.class);
    }
}