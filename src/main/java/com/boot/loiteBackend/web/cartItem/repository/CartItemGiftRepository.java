package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.entity.CartItemGiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemGiftRepository extends JpaRepository<CartItemGiftEntity, Long> {
    List<CartItemGiftEntity> findByCartItemId(Long cartItemId);
    void deleteByCartItemId(Long cartItemId);
}

