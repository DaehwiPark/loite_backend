package com.boot.loiteBackend.web.support.resource.general.repository;

import com.boot.loiteBackend.domain.support.resource.general.entity.SupportResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupportResourceRepository extends JpaRepository<SupportResourceEntity, Long> {

    @Query("""
            SELECT r
            FROM SupportResourceEntity r
            JOIN r.product p
            JOIN p.productCategory c
            WHERE (:categoryId IS NULL OR c.categoryId = :categoryId)
              AND (
                :keyword IS NULL
                OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                OR LOWER(p.productModelName) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            """)
    Page<SupportResourceEntity> searchWithKeywordAndCategory(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
