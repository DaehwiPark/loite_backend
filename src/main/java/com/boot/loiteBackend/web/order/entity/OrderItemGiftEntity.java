package com.boot.loiteBackend.web.order.entity;

import com.boot.loiteBackend.admin.product.gift.entity.AdminProductGiftEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_order_item_gift")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemGiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_GIFT_ID")
    private Long orderItemGiftId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ITEM_ID", nullable = false)
    private OrderItemEntity orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_GIFT_ID", nullable = false)
    private AdminProductGiftEntity productGift;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
}

