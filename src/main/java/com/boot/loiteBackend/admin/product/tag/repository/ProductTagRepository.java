package com.boot.loiteBackend.admin.product.tag.repository;

import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
import com.boot.loiteBackend.admin.product.tag.entity.ProductTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTagEntity, Long> {
    void deleteByProduct(ProductEntity product);

    void deleteAllByTag_TagId(Long tagId);
}
