package com.boot.loiteBackend.admin.terms.repository;

import com.boot.loiteBackend.admin.terms.entity.AdminTermsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTermsRepository extends JpaRepository<AdminTermsEntity, Long> {

    @Query("SELECT t FROM AdminTermsEntity t WHERE " +
            "(:keyword IS NULL OR LOWER(t.termsTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.termsContent) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<AdminTermsEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
