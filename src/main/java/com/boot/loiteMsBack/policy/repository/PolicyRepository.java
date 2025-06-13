package com.boot.loiteMsBack.policy.repository;

import com.boot.loiteMsBack.policy.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {
}
