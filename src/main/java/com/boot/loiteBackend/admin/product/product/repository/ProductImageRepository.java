package com.boot.loiteBackend.admin.product.product.repository;

import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    void deleteByProduct(ProductEntity product);
}
