package com.boot.loiteMsBack.product.product.service;

import com.boot.loiteMsBack.product.brand.entity.ProductBrandEntity;
import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import com.boot.loiteMsBack.product.product.mapper.ProductMapper;
import com.boot.loiteMsBack.product.product.repository.ProductRepository;
import com.boot.loiteMsBack.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Long saveProduct(ProductRequestDto dto){
        ProductEntity product = productMapper.toEntity(dto);
        return productRepository.save(product).getProductId();
    };

    @Override
    public void updateProduct(Long productId, ProductRequestDto dto) {
        ProductEntity existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        ProductBrandEntity brand = productBrandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("브랜드가 존재하지 않습니다."));

        ProductEntity product = productMapper.toEntity(dto);
        product.setProductBrandId(brand);
        existingProduct.setProductName(dto.getProductName());
        existingProduct.setProductSerialNumber(dto.getProductSerialNumber());
        existingProduct.setProductModelName(dto.getProductModelName());
        existingProduct.setProductSummary(dto.getProductSummary());
        existingProduct.setActiveYn(dto.getActiveYn());
        existingProduct.setProductPrice(dto.getProductPrice());
    }
}
