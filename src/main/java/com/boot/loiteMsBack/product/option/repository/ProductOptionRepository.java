package com.boot.loiteMsBack.product.option.repository;

import com.boot.loiteMsBack.product.option.entity.ProductOptionEntity;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {
    void deleteByProduct(ProductEntity product);
}
