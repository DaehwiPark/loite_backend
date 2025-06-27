package com.boot.loiteBackend.admin.product.section.repository;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.section.entity.AdminProductSectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductSectionRepository extends JpaRepository<AdminProductSectionEntity, Long> {
    List<AdminProductSectionEntity> findByProduct(AdminProductEntity product);

    void deleteByProduct(AdminProductEntity product);
}