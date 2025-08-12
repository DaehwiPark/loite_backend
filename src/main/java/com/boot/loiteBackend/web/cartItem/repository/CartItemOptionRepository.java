package com.boot.loiteBackend.web.cartItem.repository;

import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.projection.AvailableOptionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemOptionRepository extends JpaRepository<CartItemEntity, Long> {
    @Query(value = """
            SELECT
                po.OPTION_ID         AS optionId,
                po.OPTION_VALUE      AS optionValue,
                po.OPTION_COLOR_CODE AS colorCode,
                CASE WHEN po.OPTION_ID = ci.PRODUCT_OPTION_ID
                     THEN ci.CART_ITEM_QUANTITY
                     ELSE 0
                END                  AS quantity
            FROM TB_PRODUCT_CART_ITEM ci
            JOIN TB_PRODUCT_OPTION po
              ON po.PRODUCT_ID = ci.PRODUCT_ID
            WHERE ci.CART_ITEM_ID = :cartItemId
            ORDER BY po.OPTION_SORT_ORDER ASC, po.OPTION_ID ASC
        """, nativeQuery = true)
    List<AvailableOptionProjection> findOptionsForCartItem(@Param("cartItemId") Long cartItemId);
}
