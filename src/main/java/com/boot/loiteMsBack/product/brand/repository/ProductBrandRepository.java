package com.boot.loiteMsBack.product.brand.repository;

import com.boot.loiteMsBack.product.brand.entity.ProductBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBrandRepository extends JpaRepository<ProductBrandEntity, Long> {
}
