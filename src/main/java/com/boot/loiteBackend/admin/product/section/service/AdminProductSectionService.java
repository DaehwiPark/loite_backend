package com.boot.loiteBackend.product.section.service;

import com.boot.loiteBackend.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.product.section.dto.AdminProductSectionRequestDto;

import java.util.List;

public interface AdminProductSectionService {
    void saveSections(AdminProductEntity product, List<AdminProductSectionRequestDto> dto);
    void updateSections(AdminProductEntity product, List<AdminProductSectionRequestDto> dto);
}
