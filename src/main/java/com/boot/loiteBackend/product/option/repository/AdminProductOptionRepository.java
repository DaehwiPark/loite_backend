package com.boot.loiteBackend.product.option.repository;

import com.boot.loiteBackend.product.option.entity.ProductOptionEntity;
import com.boot.loiteBackend.product.product.entity.AdminProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {
    void deleteByProduct(AdminProductEntity product);
}
