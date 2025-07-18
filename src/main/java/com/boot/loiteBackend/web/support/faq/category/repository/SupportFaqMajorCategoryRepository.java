package com.boot.loiteBackend.web.support.faq.category.repository;

import com.boot.loiteBackend.web.support.faq.category.entity.SupportFaqMajorCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportFaqMajorCategoryRepository extends JpaRepository<SupportFaqMajorCategoryEntity, Long> {

    List<SupportFaqMajorCategoryEntity> findAllByOrderByFaqMajorCategoryOrderAsc();
}
