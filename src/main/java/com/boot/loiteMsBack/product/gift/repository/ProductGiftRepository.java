package com.boot.loiteMsBack.product.gift.repository;

import com.boot.loiteMsBack.product.gift.entity.ProductGiftEntity;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductGiftRepository extends JpaRepository<ProductGiftEntity, Long> {
}
