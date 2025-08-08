package com.boot.loiteBackend.admin.support.faq.category.repository;

import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminSupportFaqMediumCategoryRepository extends JpaRepository<SupportFaqMediumCategoryEntity, Long> {
    List<SupportFaqMediumCategoryEntity> findByFaqMajorCategory_FaqMajorCategoryId(Long majorId);
}
