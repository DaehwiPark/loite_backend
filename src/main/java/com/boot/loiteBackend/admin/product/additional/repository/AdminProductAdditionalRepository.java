package com.boot.loiteBackend.admin.product.additional.repository;

import com.boot.loiteBackend.admin.product.additional.entity.AdminProductAdditionalEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductAdditionalRepository extends JpaRepository<AdminProductAdditionalEntity, Long> {

    List<AdminProductAdditionalEntity> findByProduct(AdminProductEntity product);
}

