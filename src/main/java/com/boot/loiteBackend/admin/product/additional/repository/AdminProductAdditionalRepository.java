package com.boot.loiteBackend.admin.product.additional.repository;

import com.boot.loiteBackend.admin.product.additional.entity.AdminProductAdditionalEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductAdditionalRepository extends JpaRepository<AdminProductAdditionalEntity, Long> {
    void deleteByProduct(AdminProductEntity product);
}

