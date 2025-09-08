package com.boot.loiteBackend.web.cartItem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_product_cart_item_additional")
public class CartItemAdditionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ITEM_ADDITIONAL_ID")
    private Long id;

    @Column(name = "CART_ITEM_ID", nullable = false)
    private Long cartItemId;

    @Column(name = "PRODUCT_ADDITIONAL_ID", nullable = false)
    private Long additionalId;

    @Column(name = "ADDITIONAL_QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}

