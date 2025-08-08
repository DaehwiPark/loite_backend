package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.dto.AvailableGiftResponseDto;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemProductGiftRepository extends JpaRepository<CartItemEntity, Long> {
    @Query("""
    SELECT new com.boot.loiteBackend.web.cartItem.dto.AvailableGiftResponseDto(
        pg.productGiftId,
        g.giftName,
        g.giftImageUrl
    )
    FROM AdminProductGiftEntity pg
    JOIN pg.gift g
    WHERE pg.product.productId = :productId
    """)
    List<AvailableGiftResponseDto> findAvailableGiftsByProductId(@Param("productId") Long productId);
}
