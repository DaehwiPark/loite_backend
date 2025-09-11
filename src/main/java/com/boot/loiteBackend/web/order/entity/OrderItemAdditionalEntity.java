package com.boot.loiteBackend.web.order.entity;

import com.boot.loiteBackend.admin.product.additional.entity.AdminProductAdditionalEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_item_additional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemAdditionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_ADDITIONAL_ID")
    private Long orderItemAdditionalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ITEM_ID", nullable = false)
    private OrderItemEntity orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDITIONAL_ID", nullable = false)
    private AdminProductAdditionalEntity productAdditional;

    @Column(name = "ADDITIONAL_PRICE", precision = 15, scale = 2, nullable = false)
    private BigDecimal additionalPrice;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
}

