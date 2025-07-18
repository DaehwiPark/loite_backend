package com.boot.loiteBackend.admin.product.order.entity;

import com.boot.loiteBackend.admin.product.order.entity.AdminProductOrderEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductOrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_ID")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private AdminProductOrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private AdminProductEntity product;

    @Column(name = "ORDER_QUANTITY")
    private Integer orderQuantity;

    @Column(name = "ORDER_UNIT_PRICE", nullable = false, precision = 15, scale = 2)
    private BigDecimal orderUnitPrice;

    @Column(name = "ORDER_TOTAL_PRICE", nullable = false, precision = 15, scale = 2)
    private BigDecimal orderTotalPrice;

    @Column(name = "ORDER_OPTION_TEXT", length = 255)
    private String orderOptionText;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
