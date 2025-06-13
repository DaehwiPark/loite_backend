package com.boot.loiteMsBack.product.product.service;

import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Long saveProduct(ProductRequestDto dto, List<MultipartFile> thumbnailImages, List<MultipartFile> detailImages, Integer mainIndex) throws IOException;

    void updateProduct(Long productId, ProductRequestDto dto);
}
