package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDto {

    @Schema(description = "상품 ID", example = "101")
    private Long productId;

    @Schema(description = "상품 옵션 ID (옵션 없는 상품이면 null)", example = "202")
    private Long optionId;

    @Schema(description = "주문 수량", example = "2")
    private int quantity;

    @Schema(description = "상품 단가", example = "30000")
    private BigDecimal unitPrice;

    @Schema(description = "할인 적용 단가", example = "29000")
    private BigDecimal discountedPrice;

    @Schema(description = "총 금액 (수량 × 단가 - 할인)", example = "58000")
    private BigDecimal totalPrice;
}

