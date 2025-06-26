package com.boot.loiteBackend.product.section.mapper;

import com.boot.loiteBackend.product.section.dto.ProductSectionRequestDto;
import com.boot.loiteBackend.product.section.dto.ProductSectionResponseDto;
import com.boot.loiteBackend.product.section.entity.ProductSectionEntity;
import com.boot.loiteBackend.product.product.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductSectionMapper {

    // RequestDto → Entity
    public ProductSectionEntity toEntity(ProductSectionRequestDto dto, ProductEntity product) {
        return ProductSectionEntity.builder()
                .product(product)
                .sectionType(dto.getSectionType())
                .sectionContent(dto.getSectionContent())
                .sectionOrder(dto.getSectionOrder())
                .build();
    }

    // Entity → ResponseDto
    public ProductSectionResponseDto toDto(ProductSectionEntity entity) {
        return ProductSectionResponseDto.builder()
                .sectionId(entity.getSectionId())
                .sectionType(entity.getSectionType())
                .sectionContent(entity.getSectionContent())
                .sectionOrder(entity.getSectionOrder())
                .build();
    }
}
