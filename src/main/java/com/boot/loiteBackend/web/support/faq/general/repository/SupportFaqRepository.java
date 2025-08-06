package com.boot.loiteBackend.web.support.faq.general.repository;

import com.boot.loiteBackend.web.support.faq.general.entity.SupportFaqEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportFaqRepository extends JpaRepository<SupportFaqEntity, Long> {

    Page<SupportFaqEntity> findByFaqCategory_FaqMediumCategoryIdAndDeleteYn(Long mediumCategoryId, String deleteYn, Pageable pageable);

    @Query("""
                SELECT f FROM SupportFaqEntity f
                WHERE f.faqCategory.faqMediumCategoryId IN :mediumIds
                  AND f.deleteYn = 'N'
                  AND (
                       :keyword IS NULL OR
                       LOWER(f.faqQuestion) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                       LOWER(f.faqAnswer) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
            """)
    Page<SupportFaqEntity> searchByMediumCategoryIdsWithKeyword(
            @Param("mediumIds") List<Long> mediumIds,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}