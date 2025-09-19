package com.boot.loiteBackend.admin.product.product.repository;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminProductRepository extends JpaRepository<AdminProductEntity, Long> {
    @Query("""
        SELECT p FROM AdminProductEntity p
        LEFT JOIN SupportResourceEntity r ON p.productId = r.product.productId
        WHERE r.product IS NULL
          AND p.deleteYn = 'N'
    """)
    List<AdminProductEntity> findProductsWithoutResource();

    Page<AdminProductEntity> findAll(Specification<AdminProductEntity> spec, Pageable pageable);

    @EntityGraph(attributePaths = "productImages")
    @Query("""
        SELECT p FROM AdminProductEntity p
        WHERE p.mainExposureYn = 'Y'
          AND p.activeYn = 'Y'
          AND p.deleteYn = 'N'
        ORDER BY p.createdAt DESC
    """)
    List<AdminProductEntity> findTop20MainExposedProducts(Pageable pageable);

    @Query("""
        SELECT p FROM AdminProductEntity p
        WHERE p.mainExposureYn = 'Y'
          AND p.activeYn = 'Y'
          AND p.deleteYn = 'N'
          AND p.productCategory.categoryPath IN :paths
    """)
    Page<AdminProductEntity> findListByCategoryPaths(@Param("paths") List<String> paths, Pageable pageable);

    @Query("""
        SELECT p FROM AdminProductEntity p
        LEFT JOIN ReviewEntity r ON p.productId = r.product.productId
        WHERE p.productCategory.categoryPath IN :paths
        AND p.deleteYn = 'N'
        GROUP BY p
        ORDER BY COUNT(r.reviewId) DESC
    """)
    Page<AdminProductEntity> findListOrderByReviewCount(@Param("paths") List<String> paths, Pageable pageable);

}
