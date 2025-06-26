package com.boot.loiteBackend.product.section.repository;

import com.boot.loiteBackend.product.product.entity.ProductEntity;
import com.boot.loiteBackend.product.section.entity.ProductSectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSectionRepository extends JpaRepository<ProductSectionEntity, Long> {
    List<ProductSectionEntity> findByProduct(ProductEntity product);

    void deleteByProduct(ProductEntity product);
}