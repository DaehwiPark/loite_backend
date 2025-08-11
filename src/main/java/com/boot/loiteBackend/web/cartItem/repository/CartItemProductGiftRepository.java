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
        g.giftImageUrl,
        g.giftStock,
        p.productName,
        po.optionValue,
        po.optionColorCode
    )
    FROM CartItemEntity ci
    JOIN AdminProductEntity p
        ON ci.productId = p.productId
    LEFT JOIN AdminProductOptionEntity po
        ON ci.productOptionId = po.optionId
    JOIN AdminProductGiftEntity pg
        ON pg.product = p
    JOIN pg.gift g
    WHERE ci.id = :cartItemId
    """)
    List<AvailableGiftResponseDto> findAvailableGiftsForReselect(@Param("cartItemId") Long cartItemId);
}