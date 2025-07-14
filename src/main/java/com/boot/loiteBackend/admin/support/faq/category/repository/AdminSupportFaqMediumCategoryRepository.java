package com.boot.loiteBackend.admin.support.faq.category.repository;

import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqMediumCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSupportFaqCategoryRepository extends JpaRepository<AdminSupportFaqMediumCategoryEntity, Long> {
}
