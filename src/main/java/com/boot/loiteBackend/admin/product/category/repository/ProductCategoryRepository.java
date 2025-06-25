package com.boot.loiteBackend.admin.product.category.repository;

import com.boot.loiteBackend.admin.product.category.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    List<ProductCategoryEntity> findAllByDeleteYn(String deleteYn);
}
