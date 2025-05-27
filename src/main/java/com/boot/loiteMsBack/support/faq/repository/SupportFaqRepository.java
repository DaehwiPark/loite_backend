package com.boot.loiteMsBack.support.faq.repository;

import com.boot.loiteMsBack.support.faq.entity.SupportFaqEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportFaqRepository extends JpaRepository<SupportFaqEntity, Long> {
    @Override
    @EntityGraph(attributePaths = "faqCategory")
    List<SupportFaqEntity> findAll();
}
