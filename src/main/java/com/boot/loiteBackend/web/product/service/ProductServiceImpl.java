package com.boot.loiteBackend.web.product.service;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.web.product.dto.ProductListResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductMainResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final AdminProductRepository productRepository;

    @Override
    public List<ProductMainResponseDto> getMainProducts() {
        // 최대 20개만 가져오도록 페이징
        PageRequest top10 = PageRequest.of(0, 10);

        List<AdminProductEntity> products = productRepository.findTop20MainExposedProducts(top10);

        return products.stream()
                .map(ProductMainResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductListResponseDto> getListProducts(Long categoryId, Pageable pageable) {
        Page<AdminProductEntity> productPage = productRepository.findListByCategoryId(categoryId, pageable);

        List<ProductListResponseDto> dtoList = productPage.getContent().stream()
                .map(ProductListResponseDto::from)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, productPage.getTotalElements());
    }

}
