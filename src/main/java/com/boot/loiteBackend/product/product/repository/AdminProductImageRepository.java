package com.boot.loiteBackend.product.product.repository;

import com.boot.loiteBackend.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.product.product.entity.AdminProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductImageRepository extends JpaRepository<AdminProductImageEntity, Long> {
    void deleteByProduct(AdminProductEntity product);
}
