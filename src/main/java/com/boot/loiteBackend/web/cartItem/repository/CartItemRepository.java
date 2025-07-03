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

    Optional<CartItemEntity> findByUserIdAndProductIdAndOptionText(Long userId, Long productId, String optionText);

    @Query(value = """
        SELECT
            ci.CART_ITEM_ID         AS cartItemId,
            p.PRODUCT_ID            AS productId,
            p.PRODUCT_NAME          AS productName,
            b.BRAND_NAME            AS brandName,
            i.IMAGE_URL             AS thumbnailUrl,
            ci.cart_item_option_text AS optionText,
            ci.cart_item_quantity    AS quantity,
            p.SALE_PRICE            AS unitPrice,
            CASE ci.CHECKED_YN WHEN '1' THEN TRUE ELSE FALSE END AS checked
        FROM tb_product_cart_item ci
        JOIN tb_product p ON ci.PRODUCT_ID = p.PRODUCT_ID
        LEFT JOIN tb_product_brand b ON p.BRAND_ID = b.BRAND_ID
        LEFT JOIN tb_product_image i ON p.PRODUCT_ID = i.PRODUCT_ID AND i.image_sort_order = 1
        WHERE ci.USER_ID = :userId
          AND ci.CREATED_AT >= :minDate
    """, nativeQuery = true)
    List<CartItemProjection> findCartItemDetailsByUserId(@Param("userId") Long userId, @Param("minDate") LocalDateTime minDate);
}
