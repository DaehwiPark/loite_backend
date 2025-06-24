package com.boot.loiteMsBack.policy.repository;

import com.boot.loiteMsBack.policy.entity.PolicyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {
    @Query("SELECT p FROM PolicyEntity p WHERE " +
            "LOWER(p.policyTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.policyContent) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PolicyEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
