package com.boot.loiteBackend.admin.product.option.repository;

import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductOptionRepository extends JpaRepository<AdminProductOptionEntity, Long> {
    void deleteByProduct(AdminProductEntity product);
}
