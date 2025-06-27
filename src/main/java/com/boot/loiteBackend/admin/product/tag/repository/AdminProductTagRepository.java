package com.boot.loiteBackend.admin.product.tag.repository;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.tag.entity.AdminProductTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductTagRepository extends JpaRepository<AdminProductTagEntity, Long> {
    void deleteByProduct(AdminProductEntity product);

    void deleteAllByTag_TagId(Long tagId);
}
