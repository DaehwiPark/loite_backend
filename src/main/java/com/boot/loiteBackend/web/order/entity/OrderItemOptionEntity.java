package com.boot.loiteBackend.web.order.entity;

import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_item_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_OPTION_ID")
    private Long orderItemOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ITEM_ID", nullable = false)
    private OrderItemEntity orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_ID", nullable = false)
    private AdminProductOptionEntity productOption;

    @Column(name = "ADDITIONAL_PRICE", precision = 15, scale = 2, nullable = false)
    private BigDecimal additionalPrice;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
}

