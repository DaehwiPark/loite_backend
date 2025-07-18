package com.boot.loiteBackend.admin.product.section.mapper;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.section.dto.AdminProductSectionRequestDto;
import com.boot.loiteBackend.admin.product.section.dto.AdminProductSectionResponseDto;
import com.boot.loiteBackend.admin.product.section.entity.AdminProductSectionEntity;
import org.springframework.stereotype.Component;

@Component
public class AdminProductSectionMapper {

    // RequestDto → Entity
    public AdminProductSectionEntity toEntity(AdminProductSectionRequestDto dto, AdminProductEntity product) {
        return AdminProductSectionEntity.builder()
                .product(product)
                .sectionType(dto.getSectionType())
                .sectionContent(dto.getSectionContent())
                .sectionOrder(dto.getSectionOrder())
                .build();
    }

    // Entity → ResponseDto
    public AdminProductSectionResponseDto toDto(AdminProductSectionEntity entity) {
        return AdminProductSectionResponseDto.builder()
                .sectionId(entity.getSectionId())
                .sectionType(entity.getSectionType())
                .sectionContent(entity.getSectionContent())
                .sectionOrder(entity.getSectionOrder())
                .build();
    }
}
