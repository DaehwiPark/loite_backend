package com.boot.loiteBackend.web.payment.entity;

import com.boot.loiteBackend.web.order.entity.OrderEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private OrderEntity order;

    @Column(name = "MERCHANT_UID", nullable = false, unique = true, length = 100)
    private String merchantUid;

    @Column(name = "IMP_UID", unique = true, length = 100)
    private String impUid;

    @Column(name = "PG_TID", length = 100)
    private String pgTid;

    @Column(name = "PG_PROVIDER", nullable = false, length = 32)
    private String pgProvider;

    @Column(name = "PAYMENT_METHOD", nullable = false, length = 32)
    private String paymentMethod;

    @Column(name = "PAYMENT_TOTAL_AMOUNT", nullable = false, precision = 12, scale = 0)
    private BigDecimal paymentTotalAmount;

    @Column(name = "PAYMENT_AMOUNT_APPROVED", nullable = false, precision = 12, scale = 0)
    private BigDecimal paymentAmountApproved;

    @Builder.Default
    @Column(name = "PAYMENT_CURRENCY", nullable = false, length = 3)
    private String paymentCurrency = "KRW";

    @Column(name = "PAYMENT_STATUS", nullable = false, length = 24)
    private String status;

    @Column(name = "RECEIPT_URL", length = 255)
    private String receiptUrl;

    @Column(name = "REQUESTED_AT")
    private LocalDateTime requestedAt;

    @Column(name = "PAID_AT")
    private LocalDateTime paidAt;

    @Column(name = "CANCELED_AT")
    private LocalDateTime canceledAt;

    @Lob
    @Column(name = "RAW_PAYLOAD")
    private String rawPayload;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
