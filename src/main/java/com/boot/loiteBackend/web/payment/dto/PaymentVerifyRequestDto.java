package com.boot.loiteBackend.web.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerifyRequestDto {

    @Schema(description = "주문 번호", example = "ORD-001")
    private String paymentId;
}
