package com.boot.loiteBackend.admin.product.product.repository;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminProductRepository extends JpaRepository<AdminProductEntity, Long> {
    @Query("""
        SELECT p FROM AdminProductEntity p
        LEFT JOIN AdminSupportResourceEntity r ON p.productId = r.product.productId
        WHERE r.product IS NULL
          AND p.deleteYn = 'N'
    """)
    List<AdminProductEntity> findProductsWithoutResource();

    Page<AdminProductEntity> findAll(Specification<AdminProductEntity> spec, Pageable pageable);
}
