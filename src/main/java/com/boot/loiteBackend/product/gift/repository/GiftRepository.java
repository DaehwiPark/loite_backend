package com.boot.loiteBackend.product.gift.repository;


import com.boot.loiteBackend.product.gift.dto.GiftRequestDto;
import com.boot.loiteBackend.product.gift.entity.GiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftRepository extends JpaRepository<GiftEntity, Long> {
    List<GiftEntity> findByDeleteYn(String deleteYn);
}
