package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.dto.CartItemGiftResponseDto;
import com.boot.loiteBackend.web.cartItem.entity.CartItemGiftEntity;
import com.boot.loiteBackend.web.cartItem.projection.CartItemGiftProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemGiftRepository extends JpaRepository<CartItemGiftEntity, Long> {
    @Query(value = """
    SELECT
        cig.CART_ITEM_ID        AS cartItemId,
        cig.PRODUCT_GIFT_ID     AS productGiftId,
        cig.GIFT_QUANTITY       AS quantity,
        g.GIFT_NAME             AS giftName,
        g.GIFT_IMAGE_URL        AS giftImageUrl,
        g.GIFT_STOCK            AS giftStock
    FROM TB_PRODUCT_CART_ITEM_GIFT cig
    JOIN TB_PRODUCT_GIFT pg ON cig.PRODUCT_GIFT_ID = pg.PRODUCT_GIFT_ID
    JOIN TB_GIFT g ON pg.GIFT_ID = g.GIFT_ID
    WHERE cig.CART_ITEM_ID IN (:cartItemIds)
    """, nativeQuery = true)
    List<CartItemGiftProjection> findGiftDetailsByCartItemIds(@Param("cartItemIds") List<Long> cartItemIds);


    List<CartItemGiftEntity> findByCartItemId(Long cartItemId);
    void deleteByCartItemId(Long cartItemId);

    Optional<CartItemGiftEntity> findByCartItemIdAndGiftId(Long id, Long productGiftId);
}

