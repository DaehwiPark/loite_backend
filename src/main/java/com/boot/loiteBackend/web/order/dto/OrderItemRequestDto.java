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

    @Schema(description = "상품 ID", example = "2")
    private Long productId;

    @Schema(description = "상품 옵션 ID (없으면 null)", example = "3")
    private Long optionId;

    @Schema(description = "주문 수량", example = "1")
    private int quantity;
}

