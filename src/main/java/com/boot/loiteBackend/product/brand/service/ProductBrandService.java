package com.boot.loiteBackend.product.brand.service;

import com.boot.loiteBackend.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteBackend.product.brand.dto.ProductBrandResponseDto;

import java.util.List;

public interface ProductBrandService {

    Long saveBrand(ProductBrandRequestDto dto);
    List<ProductBrandResponseDto> getAllBrands();
    void updateBrand(Long brandId, ProductBrandRequestDto dto);
    void deleteBrand(Long brandId);
}
