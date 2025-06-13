package com.boot.loiteMsBack.product.category.repository;

import com.boot.loiteMsBack.product.category.dto.ProductCategoryResponseDto;
import com.boot.loiteMsBack.product.category.entity.ProductCategoryEntity;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
}
