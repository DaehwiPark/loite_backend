package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.entity.CartItemAdditionalEntity;
import com.boot.loiteBackend.web.cartItem.entity.CartItemGiftEntity;
import com.boot.loiteBackend.web.cartItem.projection.CartItemAdditionalProjection;
import com.boot.loiteBackend.web.cartItem.projection.CartItemGiftProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemAdditionalRepository extends JpaRepository<CartItemAdditionalEntity, Long> {
    @Query(value = """
    SELECT
        cia.CART_ITEM_ID              AS cartItemId,
        cia.PRODUCT_ADDITIONAL_ID     AS productAdditionalId,
        cia.ADDITIONAL_QUANTITY       AS quantity,
        a.ADDITIONAL_NAME             AS additionalName,
        a.ADDITIONAL_IMAGE_URL        AS additionalImageUrl,
        a.ADDITIONAL_STOCK            AS additionalStock
    FROM TB_PRODUCT_CART_ITEM_ADDITIONAL cia
    JOIN TB_PRODUCT_ADDITIONAL pa ON cia.PRODUCT_ADDITIONAL_ID = pa.PRODUCT_ADDITIONAL_ID
    JOIN TB_ADDITIONAL a ON pa.ADDITIONAL_ID = a.ADDITIONAL_ID
    WHERE cia.CART_ITEM_ID IN (:cartItemIds)
    """, nativeQuery = true)
    List<CartItemAdditionalProjection> findAdditionalDetailsByCartItemIds(@Param("cartItemIds") List<Long> cartItemIds);

    Optional<CartItemAdditionalEntity> findByCartItemIdAndAdditionalId(Long id, Long additionalId);
}
