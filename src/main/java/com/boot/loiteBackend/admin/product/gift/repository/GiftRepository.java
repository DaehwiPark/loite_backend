package com.boot.loiteBackend.admin.product.gift.repository;

import com.boot.loiteBackend.admin.product.gift.entity.GiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftRepository extends JpaRepository<GiftEntity, Long> {
    List<GiftEntity> findByDeleteYn(String deleteYn);
}
