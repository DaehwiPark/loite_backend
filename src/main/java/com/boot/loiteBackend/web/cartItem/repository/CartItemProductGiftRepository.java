package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.dto.AvailableGiftResponseDto;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.projection.AvailableGiftProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemProductGiftRepository extends JpaRepository<CartItemEntity, Long> {
    @Query(value = """
    SELECT 
        pg.PRODUCT_GIFT_ID   AS productGiftId,
        g.GIFT_NAME          AS giftName,
        g.GIFT_IMAGE_URL     AS giftImageUrl,
        g.GIFT_STOCK         AS giftStock,
        p.PRODUCT_NAME       AS productName,
        po.OPTION_VALUE      AS optionValue,
        po.OPTION_COLOR_CODE AS optionColorCode,
        COALESCE(cig.GIFT_QUANTITY, 0) AS quantity,
        g.SOLD_OUT_YN      AS giftSoldOutYn
    FROM TB_PRODUCT_CART_ITEM ci
    JOIN TB_PRODUCT p
        ON ci.PRODUCT_ID = p.PRODUCT_ID
    LEFT JOIN TB_PRODUCT_OPTION po
        ON ci.PRODUCT_OPTION_ID = po.OPTION_ID
    JOIN TB_PRODUCT_GIFT pg
        ON pg.PRODUCT_ID = p.PRODUCT_ID
    JOIN TB_GIFT g
        ON g.GIFT_ID = pg.GIFT_ID
    LEFT JOIN TB_PRODUCT_CART_ITEM_GIFT cig
        ON cig.CART_ITEM_ID = ci.CART_ITEM_ID
    AND cig.PRODUCT_GIFT_ID = pg.PRODUCT_GIFT_ID
    WHERE ci.CART_ITEM_ID = :cartItemId
    """, nativeQuery = true)
    List<AvailableGiftProjection> findAvailableGiftsForReselect(@Param("cartItemId") Long cartItemId);
}