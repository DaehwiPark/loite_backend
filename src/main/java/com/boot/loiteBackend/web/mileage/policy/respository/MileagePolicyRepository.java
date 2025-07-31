package com.boot.loiteBackend.web.mileage.policy.respository;

import com.boot.loiteBackend.web.mileage.policy.entity.MileagePolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MileagePolicyRepository extends JpaRepository<MileagePolicyEntity, Long> {
    Optional<MileagePolicyEntity> findByMileagePolicyCodeAndMileagePolicyYn(String code, String yn);
}