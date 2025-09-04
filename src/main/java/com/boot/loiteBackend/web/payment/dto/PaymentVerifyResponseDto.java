package com.boot.loiteBackend.web.payment.dto;

import com.boot.loiteBackend.web.payment.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentVerifyResponseDto {

    @Schema(description = "주문 ID (PK)", example = "1")
    private Long orderId;

    @Schema(description = "주문 번호", example = "ORD-20250901-000093")
    private String orderNumber;

    @Schema(description = "결제 ID (PK)", example = "10")
    private Long paymentId;

    @Schema(description = "결제 상태", example = "PAID")
    private PaymentStatus paymentStatus;
}
