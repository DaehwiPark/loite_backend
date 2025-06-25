package com.boot.loiteBackend.terms.repository;

import com.boot.loiteBackend.terms.entity.TermsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<TermsEntity, Long> {

    @Query("SELECT t FROM TermsEntity t WHERE " +
            "(:keyword IS NULL OR LOWER(t.termsTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.termsContent) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<TermsEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
