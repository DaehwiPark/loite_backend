package com.boot.loiteBackend.admin.support.faq.category.repository;

import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMajorCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSupportFaqMajorCategoryRepository extends JpaRepository<SupportFaqMajorCategoryEntity, Long> {
}
