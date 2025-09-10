package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.entity.CartItemOptionEntity;
import com.boot.loiteBackend.web.cartItem.projection.AvailableOptionProjection;
import com.boot.loiteBackend.web.cartItem.projection.CartItemGiftProjection;
import com.boot.loiteBackend.web.cartItem.projection.CartItemOptionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemOptionRepository extends JpaRepository<CartItemOptionEntity, Long> {
    @Query(value = """
            SELECT
                po.OPTION_ID         AS optionId,
                po.OPTION_VALUE      AS optionValue,
                po.OPTION_COLOR_CODE AS colorCode,
                po.OPTION_STOCK      AS optionStock,
                COALESCE(cio.QUANTITY, 0) AS quantity
            FROM TB_PRODUCT_OPTION po
            LEFT JOIN TB_PRODUCT_CART_ITEM_OPTION cio
              ON cio.PRODUCT_OPTION_ID = po.OPTION_ID
             AND cio.CART_ITEM_ID = :cartItemId
            WHERE po.PRODUCT_ID = (
                SELECT ci.PRODUCT_ID 
                FROM TB_PRODUCT_CART_ITEM ci 
                WHERE ci.CART_ITEM_ID = :cartItemId
            )
            ORDER BY po.OPTION_SORT_ORDER ASC, po.OPTION_ID ASC
        """, nativeQuery = true)
    List<AvailableOptionProjection> findOptionsForCartItem(@Param("cartItemId") Long cartItemId);

    @Query("SELECT o.optionId FROM CartItemOptionEntity o WHERE o.cartItemId = :cartItemId")
    List<Long> findOptionIdsByCartItemId(@Param("cartItemId") Long cartItemId);

    void deleteByCartItemId(Long cartItemId);

    @Query(value = """
    SELECT
        cio.CART_ITEM_ID            AS cartItemId,
        cio.PRODUCT_OPTION_ID       AS productGiftId,
        o.OPTION_ID                 AS optionId,
        o.OPTION_VALUE              AS optionValue,
        o.OPTION_TYPE               AS optionType,
        o.OPTION_ADDITIONAL_PRICE   AS optionAdditionalPrice,
        o.OPTION_STOCK              AS optionStock
    FROM TB_PRODUCT_CART_ITEM_OPTION cio
    JOIN TB_PRODUCT_OPTION po ON cio.PRODUCT_OPTION_ID = po.OPTION_ID
    JOIN TB_PRODUCT_OPTION o ON po.OPTION_ID = o.OPTION_ID
    WHERE cio.CART_ITEM_ID IN (:cartItemIds)
    """, nativeQuery = true)
    List<CartItemOptionProjection> findOptionDetailsByCartItemIds(List<Long> cartItemIds);
}

