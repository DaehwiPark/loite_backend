package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDto {

    @Schema(description = "상품 ID", example = "2")
    private Long productId;

    @Schema(description = "주문 수량", example = "1")
    private int quantity;

    private List<OrderOptionRequestDto> options;

    private List<OrderAdditionalRequestDto> additionals;

    private List<OrderGiftRequestDto> gifts;

}

