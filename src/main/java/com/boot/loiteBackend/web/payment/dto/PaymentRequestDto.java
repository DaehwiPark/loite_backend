package com.boot.loiteBackend.web.payment.dto;

import com.boot.loiteBackend.web.payment.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

    @Schema(description = "주문 번호", example = "1")
    private String merchantUid;

    @Schema(description = "포트원 결제 고유번호", example = "1")
    private String impUid;

    @Schema(description = "PG사", example = "TOSS, KAKAO, INICIS")
    private String pgProvider;

    @Schema(description = "결제 수단", example = "CARD")
    private String paymentMethod;

    @Schema(description = "총 결제 요청 금액", example = "1000")
    private BigDecimal paymentTotalAmount;

    @Schema(description = "결제 상태", example = "READY")
    private PaymentStatus paymentStatus;

    @Schema(description = "PG사 응답 (JSON 문자열)", example = "example")
    private String rawPayLoad;
}
