package com.boot.loiteBackend.admin.product.brand.service;

import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandRequestDto;
import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandResponseDto;

import java.util.List;

public interface AdminProductBrandService {

    Long saveBrand(AdminProductBrandRequestDto dto);
    List<AdminProductBrandResponseDto> getAllBrands();
    void updateBrand(Long brandId, AdminProductBrandRequestDto dto);
    void deleteBrand(Long brandId);
}
