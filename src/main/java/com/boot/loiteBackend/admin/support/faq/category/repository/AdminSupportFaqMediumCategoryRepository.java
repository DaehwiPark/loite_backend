package com.boot.loiteBackend.admin.support.faq.category.repository;

import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqMediumCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminSupportFaqMediumCategoryRepository extends JpaRepository<AdminSupportFaqMediumCategoryEntity, Long> {
    List<AdminSupportFaqMediumCategoryEntity> findByFaqMajorCategory_FaqMajorCategoryId(Long majorId);
}
