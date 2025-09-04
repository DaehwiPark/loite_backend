package com.boot.loiteBackend.admin.product.additional.repository;

import com.boot.loiteBackend.admin.product.additional.entity.AdminAdditionalEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminAdditionalRepository extends JpaRepository<AdminAdditionalEntity, Long> {

    Optional<AdminAdditionalEntity> findByAdditionalIdAndDeleteYn(Long additionalId, String deleteYn);

    Page<AdminAdditionalEntity> findByDeleteYn(String deleteYn, Pageable pageable);
}
