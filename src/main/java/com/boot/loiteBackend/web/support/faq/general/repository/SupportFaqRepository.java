package com.boot.loiteBackend.web.support.faq.general.repository;

import com.boot.loiteBackend.web.support.faq.general.entity.SupportFaqEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportFaqRepository extends JpaRepository<SupportFaqEntity, Long> {

    Page<SupportFaqEntity> findByFaqCategory_FaqMediumCategoryIdAndDeleteYn(Long mediumCategoryId, String deleteYn, Pageable pageable);

    Page<SupportFaqEntity> findByFaqCategory_FaqMediumCategoryIdInAndDeleteYn(List<Long> mediumIds, String n, Pageable pageable);
}