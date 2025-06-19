package com.boot.loiteMsBack.product.category.service;

import com.boot.loiteMsBack.product.category.dto.ProductCategoryRequestDto;
import com.boot.loiteMsBack.product.category.dto.ProductCategoryResponseDto;

import java.util.List;

public interface ProductCategoryService {
    Long saveCategory(ProductCategoryRequestDto dto);
    List<ProductCategoryResponseDto> getAllCategory();
}
