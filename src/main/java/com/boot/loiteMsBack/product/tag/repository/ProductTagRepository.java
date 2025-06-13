package com.boot.loiteMsBack.product.tag.repository;

import com.boot.loiteMsBack.product.tag.entity.ProductTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTagEntity, Long> {
}
