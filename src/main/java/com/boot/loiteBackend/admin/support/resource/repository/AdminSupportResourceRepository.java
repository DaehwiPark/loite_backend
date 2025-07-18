package com.boot.loiteBackend.admin.support.resource.repository;

import com.boot.loiteBackend.admin.support.resource.entity.AdminSupportResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSupportResourceRepository extends JpaRepository<AdminSupportResourceEntity, Long> {

    @Query("""
        SELECT r FROM AdminSupportResourceEntity r
        WHERE LOWER(r.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(r.resourceFileName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<AdminSupportResourceEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
