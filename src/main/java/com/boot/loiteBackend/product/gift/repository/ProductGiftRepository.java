package com.boot.loiteBackend.product.gift.repository;

import com.boot.loiteBackend.product.gift.entity.ProductGiftEntity;
import com.boot.loiteBackend.product.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductGiftRepository extends JpaRepository<ProductGiftEntity, Long> {
}
