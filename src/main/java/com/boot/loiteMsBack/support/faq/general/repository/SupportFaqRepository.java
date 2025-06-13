package com.boot.loiteMsBack.support.faq.general.repository;

import com.boot.loiteMsBack.support.faq.general.entity.SupportFaqEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportFaqRepository extends JpaRepository<SupportFaqEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "faqCategory")
    List<SupportFaqEntity> findAll();

    @Query("""
        SELECT f FROM SupportFaqEntity f
        WHERE LOWER(f.faqQuestion) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(f.faqAnswer) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<SupportFaqEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
