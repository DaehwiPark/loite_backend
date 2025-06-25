package com.boot.loiteBackend.support.resource.repository;

import com.boot.loiteBackend.support.resource.entity.SupportResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupportResourceRepository extends JpaRepository<SupportResourceEntity, Long> {

    @Query("""
        SELECT r FROM SupportResourceEntity r
        WHERE LOWER(r.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(r.resourceFileName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<SupportResourceEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
