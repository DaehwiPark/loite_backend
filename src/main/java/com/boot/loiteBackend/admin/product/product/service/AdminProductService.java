package com.boot.loiteBackend.admin.product.product.service;

import com.boot.loiteBackend.admin.product.product.dto.AdminProductDetailResponseDto;
import com.boot.loiteBackend.admin.product.product.dto.AdminProductRequestDto;
import com.boot.loiteBackend.admin.product.product.dto.AdminProductListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {
    Long saveProduct(AdminProductRequestDto dto, List<MultipartFile> thumbnailImages);

    void updateProduct(AdminProductRequestDto dto, List<MultipartFile> thumbnailImages);

    Page<AdminProductListResponseDto> getPagedProducts(String keyword, Pageable pageable);

    AdminProductDetailResponseDto getAllProductDetail(Long productId);

    void deleteProduct(Long productId);
}
