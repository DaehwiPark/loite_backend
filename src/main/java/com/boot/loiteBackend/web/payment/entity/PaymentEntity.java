package com.boot.loiteBackend.web.payment.entity;

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

    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;   // 주문 PK (FK는 직접 안 건다, 그냥 참조)

    @Column(name = "MERCHANT_UID", nullable = false, unique = true, length = 100)
    private String merchantUid; // 우리 시스템 주문번호

    @Column(name = "IMP_UID", unique = true, length = 100)
    private String impUid; // 포트원 결제번호

    @Column(name = "PG_TID", length = 100)
    private String pgTid;  // PG사 거래번호

    @Column(name = "PG_PROVIDER", nullable = false, length = 32)
    private String pgProvider;  // PG사 종류 (TOSS, INICIS, KAKAOPAY 등)

    @Column(name = "METHOD", nullable = false, length = 32)
    private String method; // 결제수단 (CARD, VIRTUAL_ACCOUNT 등)

    @Column(name = "AMOUNT_TOTAL", nullable = false, precision = 12, scale = 0)
    private BigDecimal amountTotal; // 요청 총 금액

    @Column(name = "AMOUNT_APPROVED", nullable = false, precision = 12, scale = 0)
    private BigDecimal amountApproved; // 승인된 금액 (부분취소 반영)

    @Column(name = "CURRENCY", nullable = false, length = 3)
    private String currency = "KRW"; // 통화 (기본 KRW)

    @Column(name = "STATUS", nullable = false, length = 24)
    private String status; // 결제 상태 (READY, PAID, FAILED, CANCELED 등)

    @Column(name = "RECEIPT_URL", length = 255)
    private String receiptUrl; // 영수증 URL

    @Column(name = "REQUESTED_AT")
    private LocalDateTime requestedAt; // 결제 요청 시각

    @Column(name = "PAID_AT")
    private LocalDateTime paidAt; // 결제 승인 완료 시각

    @Column(name = "CANCELED_AT")
    private LocalDateTime canceledAt; // 결제 취소 완료 시각

    @Lob
    @Column(name = "RAW_PAYLOAD")
    private String rawPayload; // PG/포트원 원본 응답 데이터

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
