package com.boot.loiteBackend.admin.product.brand.repository;

import com.boot.loiteBackend.admin.product.brand.entity.ProductBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBrandRepository extends JpaRepository<ProductBrandEntity, Long> {
    List<ProductBrandEntity> findAllByDeleteYn(String deleteYn);
}
