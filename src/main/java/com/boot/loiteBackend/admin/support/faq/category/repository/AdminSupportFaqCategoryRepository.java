package com.boot.loiteBackend.admin.support.faq.category.repository;

import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSupportFaqCategoryRepository extends JpaRepository<AdminSupportFaqCategoryEntity, Long> {
}
