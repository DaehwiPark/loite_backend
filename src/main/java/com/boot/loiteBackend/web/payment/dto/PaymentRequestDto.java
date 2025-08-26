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

    @Schema(description = "주문 번호", example = "ORD-001")
    private String merchantUid;

    @Schema(description = "포트원 트랜잭션 ID", example = "example")
    private String txId;
}
