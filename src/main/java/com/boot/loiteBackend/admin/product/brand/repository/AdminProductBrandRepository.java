package com.boot.loiteBackend.product.brand.repository;

import com.boot.loiteBackend.product.brand.entity.AdminProductBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductBrandRepository extends JpaRepository<AdminProductBrandEntity, Long> {
    List<AdminProductBrandEntity> findAllByDeleteYn(String deleteYn);
}
