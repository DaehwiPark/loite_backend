package com.boot.loiteBackend.product.product.service;

import com.boot.loiteBackend.product.product.dto.ProductDetailResponseDto;
import com.boot.loiteBackend.product.product.dto.ProductRequestDto;
import com.boot.loiteBackend.product.product.dto.ProductListResponseDto;
import com.boot.loiteBackend.product.product.dto.ProductUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Long saveProduct(ProductRequestDto dto, List<MultipartFile> thumbnailImages, Integer mainIndex) throws IOException;

    void updateProduct(ProductRequestDto dto, List<MultipartFile> thumbnailImages, Integer mainIndex) throws IOException;

    Page<ProductListResponseDto> getPagedProducts(String keyword, Pageable pageable);

    ProductDetailResponseDto getAllProductDetail(Long productId);

    void deleteProduct(Long productId);
}
