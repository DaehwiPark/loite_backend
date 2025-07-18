package com.boot.loiteBackend.admin.product.gift.repository;

import com.boot.loiteBackend.admin.product.gift.entity.AdminProductGiftEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductGiftRepository extends JpaRepository<AdminProductGiftEntity, Long> {
    void deleteByProduct(AdminProductEntity product);
}
