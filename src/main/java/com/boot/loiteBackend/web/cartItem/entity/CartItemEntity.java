package com.boot.loiteBackend.web.cartItem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_cart_item",
        uniqueConstraints = @UniqueConstraint(name = "UNQ_USER_PRODUCT_OPTION", columnNames = {"USER_ID", "PRODUCT_ID", "cart_item_option_text"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ITEM_ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "PRODUCT_OPTION_ID")
    private Long productOptionId;

    @Column(name = "PRODUCT_GIFT_ID")
    private Long giftId;

    @Column(name = "cart_item_quantity", nullable = false)
    private Integer quantity;

    @Builder.Default
    @Column(name = "CHECKED_YN", length = 1)
    private String checkedYn = "1";

    @Builder.Default
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
