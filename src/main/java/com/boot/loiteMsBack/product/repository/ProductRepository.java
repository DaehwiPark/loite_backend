package com.boot.loiteMsBack.product.repository;

import com.boot.loiteMsBack.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
