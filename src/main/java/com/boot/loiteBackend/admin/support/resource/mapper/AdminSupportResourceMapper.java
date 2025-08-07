package com.boot.loiteBackend.admin.support.resource.mapper;

import com.boot.loiteBackend.admin.support.resource.dto.AdminSupportResourceDto;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.domain.support.resource.general.entity.SupportResourceEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSupportResourceMapper {

    private final ModelMapper modelMapper;

    public AdminSupportResourceDto toDto(SupportResourceEntity entity) {
        if (entity == null) return null;

        AdminSupportResourceDto dto = modelMapper.map(entity, AdminSupportResourceDto.class);

        AdminProductEntity product = entity.getProduct();
        if (product != null) {
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setProductModelName(product.getProductModelName());
        }
        return dto;
    }

    public SupportResourceEntity toEntity(AdminSupportResourceDto dto) {
        return modelMapper.map(dto, SupportResourceEntity.class);
    }
}