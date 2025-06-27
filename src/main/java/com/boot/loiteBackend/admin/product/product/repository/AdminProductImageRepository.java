package com.boot.loiteBackend.admin.product.product.repository;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductImageRepository extends JpaRepository<AdminProductImageEntity, Long> {
    void deleteByProduct(AdminProductEntity product);
}
