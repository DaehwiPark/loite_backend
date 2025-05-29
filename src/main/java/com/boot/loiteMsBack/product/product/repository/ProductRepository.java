package com.boot.loiteMsBack.product.product.repository;

import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
