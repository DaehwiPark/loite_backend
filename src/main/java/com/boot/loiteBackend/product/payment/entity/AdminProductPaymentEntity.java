package com.boot.loiteBackend.product.payment.entity;

import com.boot.loiteBackend.product.order.entity.AdminProductOrderEntity;
import com.boot.loiteBackend.product.payment.enums.PaymentMethodType;
import com.boot.loiteBackend.product.payment.enums.PaymentStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductPaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private AdminProductOrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_METHOD", nullable = false, length = 20)
    private PaymentMethodType paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", nullable = false, length = 20)
    private PaymentStatusType paymentStatus;

    @Column(name = "PAYMENT_AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "PG_TID", length = 100)
    private String pgTid;

    @Column(name = "PAID_AT")
    private LocalDateTime paidAt;

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
