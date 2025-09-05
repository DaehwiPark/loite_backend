package com.boot.loiteBackend.admin.product.gift.repository;

import com.boot.loiteBackend.admin.product.gift.entity.AdminProductGiftEntity;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductGiftRepository extends JpaRepository<AdminProductGiftEntity, Long> {

    List<AdminProductGiftEntity> findByProduct(AdminProductEntity product);

    List<AdminProductGiftEntity> findByProductAndDeleteYn(AdminProductEntity entity, String deleteYn);
}
