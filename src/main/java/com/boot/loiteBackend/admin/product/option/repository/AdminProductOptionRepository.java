package com.boot.loiteBackend.admin.product.option.repository;

import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductOptionRepository extends JpaRepository<AdminProductOptionEntity, Long> {

    List<AdminProductOptionEntity> findByProduct(AdminProductEntity product);

    List<AdminProductOptionEntity> findByProductAndDeleteYn(AdminProductEntity product, String deleteYn);
}
