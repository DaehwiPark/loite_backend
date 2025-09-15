package com.boot.loiteBackend.admin.product.product.repository;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminProductImageRepository extends JpaRepository<AdminProductImageEntity, Long> {

    void deleteByProduct(AdminProductEntity product);

    Optional<AdminProductImageEntity> findFirstByProduct_ProductIdAndImageTypeAndActiveYnOrderByImageSortOrderAsc(
            Long productId, ImageType imageType, String activeYn
    );

    Optional<AdminProductImageEntity> findFirstByProduct_ProductIdOrderByImageSortOrderAsc(Long productId);
}
