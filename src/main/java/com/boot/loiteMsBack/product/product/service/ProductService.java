package com.boot.loiteMsBack.product.product.service;

import com.boot.loiteMsBack.product.product.dto.ProductDetailResponseDto;
import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.dto.ProductListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Long saveProduct(ProductRequestDto dto, List<MultipartFile> thumbnailImages, List<MultipartFile> detailImages, Integer mainIndex) throws IOException;

    void updateProduct(ProductRequestDto dto, List<MultipartFile> thumbnailImages, List<MultipartFile> detailImages, Integer mainIndex) throws IOException;

    Page<ProductListResponseDto> getPagedProducts(String keyword, Pageable pageable);

    ProductDetailResponseDto getAllProductDetail(Long productId);

    void deleteProduct(Long productId);
}
