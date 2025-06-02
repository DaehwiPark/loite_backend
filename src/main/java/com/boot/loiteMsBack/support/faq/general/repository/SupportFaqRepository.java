package com.boot.loiteMsBack.support.faq.general.repository;

import com.boot.loiteMsBack.support.faq.general.entity.SupportFaqEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportFaqRepository extends JpaRepository<SupportFaqEntity, Long> {
    @Override
    @EntityGraph(attributePaths = "faqCategory")
    List<SupportFaqEntity> findAll();
}
