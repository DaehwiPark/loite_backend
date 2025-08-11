package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.projection.CartItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByUserIdAndProductIdAndProductOptionId(Long userId, Long productId, Long productOptionId);

    @Query(value = """
    SELECT
        ci.CART_ITEM_ID            AS cartItemId,
        p.PRODUCT_ID               AS productId,
        p.PRODUCT_NAME             AS productName,
        b.BRAND_NAME               AS brandName,
        i.IMAGE_URL                AS thumbnailUrl,
        P.PRODUCT_STOCK            AS productStock,
        o.OPTION_TYPE              AS optionType,
        o.OPTION_VALUE             AS optionValue,
        o.OPTION_ADDITIONAL_PRICE  AS optionAdditionalPrice,
        o.OPTION_STOCK             AS optionStock,
        ci.CART_ITEM_QUANTITY      AS quantity,
        p.PRODUCT_PRICE            AS unitPrice,
        p.DISCOUNTED_PRICE         AS discountedPrice,
        p.DISCOUNT_RATE            AS discountRate,
        CASE ci.CHECKED_YN WHEN '1' THEN 1 ELSE 0 END AS checked,
        g.GIFT_NAME                AS giftName,
        g.GIFT_IMAGE_URL           AS giftImageUrl
    FROM TB_PRODUCT_CART_ITEM ci
    JOIN TB_PRODUCT p ON ci.PRODUCT_ID = p.PRODUCT_ID
    LEFT JOIN TB_BRAND b ON p.BRAND_ID = b.BRAND_ID
    LEFT JOIN TB_PRODUCT_IMAGE i ON p.PRODUCT_ID = i.PRODUCT_ID AND i.IMAGE_SORT_ORDER = 1
    LEFT JOIN TB_PRODUCT_OPTION o ON ci.PRODUCT_OPTION_ID = o.OPTION_ID
    LEFT JOIN TB_PRODUCT_CART_ITEM_GIFT cig ON ci.CART_ITEM_ID = cig.CART_ITEM_ID
    LEFT JOIN TB_PRODUCT_GIFT pg ON cig.PRODUCT_GIFT_ID = pg.PRODUCT_GIFT_ID
    LEFT JOIN TB_GIFT g ON pg.GIFT_ID = g.GIFT_ID
    WHERE ci.USER_ID = :userId
      AND ci.CREATED_AT >= :after
    ORDER BY ci.CREATED_AT DESC
""", nativeQuery = true)
    List<CartItemProjection> findCartItemsByUserId(@Param("userId") Long userId, @Param("after") LocalDateTime after);

    List<CartItemEntity> findByUserIdAndIdIn(Long userId, List<Long> ids);

    List<CartItemEntity> findByUserIdAndCheckedYn(Long userId, String checkedYn);
}
