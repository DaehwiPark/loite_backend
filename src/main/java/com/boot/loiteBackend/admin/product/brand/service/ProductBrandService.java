package com.boot.loiteBackend.admin.product.brand.service;

import com.boot.loiteBackend.admin.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteBackend.admin.product.brand.dto.ProductBrandResponseDto;

import java.util.List;

public interface ProductBrandService {

    Long saveBrand(ProductBrandRequestDto dto);
    List<ProductBrandResponseDto> getAllBrands();
    void updateBrand(Long brandId, ProductBrandRequestDto dto);
    void deleteBrand(Long brandId);
}
