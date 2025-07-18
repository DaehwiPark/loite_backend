package com.boot.loiteBackend.admin.policy.repository;

import com.boot.loiteBackend.admin.policy.entity.AdminPolicyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminPolicyRepository extends JpaRepository<AdminPolicyEntity, Long> {
    @Query("SELECT p FROM AdminPolicyEntity p WHERE " +
            "LOWER(p.policyTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.policyContent) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<AdminPolicyEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
