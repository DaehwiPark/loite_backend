package com.boot.loiteBackend.web.payment.dto;

import com.boot.loiteBackend.web.payment.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {
    @Schema(description = "결제 ID", example = "1")
    private Long paymentId;

    @Schema(description = "주문 번호", example = "1")
    private String merchantUid;

    @Schema(description = "포트원 결제 고유번호", example = "1")
    private String impUid;

    @Schema(description = "PG사 종류", example = "TOSS, KAKAO, INICIS")
    private String pgProvider;

    @Schema(description = "결제 수단", example = "CARD")
    private String paymentMethod;

    @Schema(description = "총 결제 요청금액", example = "1000")
    private BigDecimal paymentTotalAmount;

    @Schema(description = "승인된 금액", example = "1000")
    private BigDecimal paymentAmountApproved;

    @Schema(description = "결제 상태", example = "READY")
    private PaymentStatus paymentStatus;

    @Schema(description = "영수증 URL", example = "exmaple")
    private String receiptUrl;

    @Schema(description = "결제 승인 완료 시각", example = "yyyy/mm/dd-00:00:00")
    private LocalDateTime paidAt;

    @Schema(description = "결제 생성 시각", example = "2025-08-21T14:00:00")
    private LocalDateTime createdAt;
}
