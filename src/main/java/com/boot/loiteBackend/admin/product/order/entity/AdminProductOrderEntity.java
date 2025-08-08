package com.boot.loiteBackend.admin.product.order.entity;


import com.boot.loiteBackend.admin.product.order.enums.OrderStatusType;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "ORDER_NUMBER", nullable = false, length = 50, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS", length = 20)
    private OrderStatusType orderStatus;

    @Column(name = "ORDER_TOTAL_PRICE", nullable = false, precision = 15, scale = 2)
    private BigDecimal orderTotalPrice;

    @Column(name = "ORDER_MEMO", length = 500)
    private String orderMemo;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
