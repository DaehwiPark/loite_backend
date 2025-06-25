package com.boot.loiteBackend.product.option.repository;

import com.boot.loiteBackend.product.option.entity.ProductOptionEntity;
import com.boot.loiteBackend.product.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {
    void deleteByProduct(ProductEntity product);
}
