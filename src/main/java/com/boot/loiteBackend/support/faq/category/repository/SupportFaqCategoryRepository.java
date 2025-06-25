package com.boot.loiteBackend.support.faq.category.repository;

import com.boot.loiteBackend.support.faq.category.entity.SupportFaqCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportFaqCategoryRepository extends JpaRepository<SupportFaqCategoryEntity, Long> {
}
