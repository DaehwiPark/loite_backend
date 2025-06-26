package com.boot.loiteBackend.admin.support.faq.general.repository;

import com.boot.loiteBackend.admin.support.faq.general.entity.AdminSupportFaqEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminSupportFaqRepository extends JpaRepository<AdminSupportFaqEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "faqCategory")
    List<AdminSupportFaqEntity> findAll();

    @Query("""
        SELECT f FROM AdminSupportFaqEntity f
        WHERE LOWER(f.faqQuestion) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(f.faqAnswer) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<AdminSupportFaqEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
