package com.boot.loiteBackend.admin.product.gift.repository;


import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminGiftRepository extends JpaRepository<AdminGiftEntity, Long> {
    List<AdminGiftEntity> findByDeleteYn(String deleteYn);
}
