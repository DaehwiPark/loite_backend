package com.boot.loiteBackend.web.product.service;

import com.boot.loiteBackend.web.product.dto.ProductDetailResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductListResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductMainResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductMainResponseDto> getMainProducts();

    Page<ProductListResponseDto> getListProducts(String categoryPath, String sortType, Pageable pageable);

    ProductDetailResponseDto getDetailProducts(Long productId);
}
