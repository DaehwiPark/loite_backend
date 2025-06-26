package com.boot.loiteBackend.product.section.service;

import com.boot.loiteBackend.product.product.entity.ProductEntity;
import com.boot.loiteBackend.product.section.dto.ProductSectionRequestDto;

import java.util.List;

public interface ProductSectionService {
    void saveSections(ProductEntity product, List<ProductSectionRequestDto> dto);
    void updateSections(ProductEntity product, List<ProductSectionRequestDto> dto);
}
