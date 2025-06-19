package com.boot.loiteMsBack.product.brand.service;

import com.boot.loiteMsBack.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteMsBack.product.brand.dto.ProductBrandResponseDto;

import java.util.List;

public interface ProductBrandService {
    Long saveBrand(ProductBrandRequestDto dto);
    List<ProductBrandResponseDto> getAllBrands();
}
