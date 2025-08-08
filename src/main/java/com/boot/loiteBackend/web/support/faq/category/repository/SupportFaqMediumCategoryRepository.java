package com.boot.loiteBackend.web.support.faq.category.repository;

import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportFaqMediumCategoryRepository extends JpaRepository<SupportFaqMediumCategoryEntity, Long> {

    List<SupportFaqMediumCategoryEntity> findAllByFaqMajorCategory_FaqMajorCategoryIdOrderByFaqMediumCategoryOrderAsc(Long majorCategoryId);

    @Query("SELECT m.faqMediumCategoryId FROM SupportFaqMediumCategoryEntity m WHERE m.faqMajorCategory.faqMajorCategoryId = :majorId")
    List<Long> findIdsByMajorId(@Param("majorId") Long majorId);
}
