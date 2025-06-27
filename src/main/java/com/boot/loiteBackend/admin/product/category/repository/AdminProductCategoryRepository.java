package com.boot.loiteBackend.admin.product.category.repository;

import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductCategoryRepository extends JpaRepository<AdminProductCategoryEntity, Long> {
    List<AdminProductCategoryEntity> findAllByDeleteYn(String deleteYn);
}
