package com.boot.loiteMsBack.support.repository;

import com.boot.loiteMsBack.support.entity.SupportFaqEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<SupportFaqEntity, Long> {
    @Override
    @EntityGraph(attributePaths = "faqCategory")
    List<SupportFaqEntity> findAll();
}
