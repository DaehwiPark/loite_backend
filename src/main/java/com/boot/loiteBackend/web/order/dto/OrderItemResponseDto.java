package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    @Schema(description = "상품 ID", example = "2001")
    private Long productId;

    @Schema(description = "상품명", example = "삼성 냉장고 500L")
    private String productName;

    @Schema(description = "상품 이미지", example = "url 어쩌구 저쩌구")
    private String productImageUrl;

    @Schema(description = "수량", example = "2")
    private Integer quantity;

    @Schema(description = "단가", example = "60000")
    private BigDecimal unitPrice;

    @Schema(description = "총 금액", example = "120000")
    private BigDecimal totalPrice;

    private List<OrderOptionResponseDto> options;

    private List<OrderAdditionalResponseDto> additionals;

    private List<OrderGiftResponseDto> gifts;
}
