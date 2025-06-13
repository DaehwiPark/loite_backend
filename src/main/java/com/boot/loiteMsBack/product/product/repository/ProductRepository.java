package com.boot.loiteMsBack.product.product.repository;

import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("""
        SELECT p FROM ProductEntity p
        LEFT JOIN SupportResourceEntity r ON p.productId = r.product.productId
        WHERE r.product IS NULL
          AND p.deleteYn = 'N'
    """)
    List<ProductEntity> findProductsWithoutResource();
}
