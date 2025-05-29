package com.boot.loiteMsBack.product.product.service;

import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface ProductService {
    Long saveProduct(ProductRequestDto dto);
    void updateProduct(Long productId, ProductRequestDto dto);
}
