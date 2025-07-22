package com.boot.loiteBackend.web.support.resource.category.repository;

import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SupportResourceCategoryRepository extends Repository<AdminProductCategoryEntity, Long> {

    @Query("""
        SELECT DISTINCT c
        FROM AdminProductCategoryEntity c
        JOIN AdminProductEntity p ON p.productCategory = c
        JOIN SupportResourceEntity r ON r.product = p
        WHERE c.deleteYn = 'N'
          AND c.activeYn = 'Y'
    """)
    List<AdminProductCategoryEntity> findValidCategoriesHavingManuals();
}
