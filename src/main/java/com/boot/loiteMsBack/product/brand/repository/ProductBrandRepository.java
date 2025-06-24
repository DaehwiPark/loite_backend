package com.boot.loiteMsBack.product.brand.repository;

import com.boot.loiteMsBack.product.brand.entity.ProductBrandEntity;
import com.boot.loiteMsBack.product.category.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBrandRepository extends JpaRepository<ProductBrandEntity, Long> {
    List<ProductBrandEntity> findAllByDeleteYn(String deleteYn);
}
