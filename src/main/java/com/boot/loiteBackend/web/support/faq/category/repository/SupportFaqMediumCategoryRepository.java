package com.boot.loiteBackend.web.support.faq.category.repository;

import com.boot.loiteBackend.web.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportFaqMediumCategoryRepository extends JpaRepository<SupportFaqMediumCategoryEntity, Long> {

    List<SupportFaqMediumCategoryEntity> findAllByFaqMajorCategory_FaqMajorCategoryIdOrderByFaqMediumCategoryOrderAsc(Long majorCategoryId);
}
