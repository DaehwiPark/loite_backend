package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemOptionResponseDto {

    @Schema(description = "장바구니 ID", example = "1")
    private Long cartItemId;

    @Schema(description = "옵션 ID", example = "3")
    private Long optionId;

    @Schema(description = "선택한 옵션 타입 (예: 색상)", example = "색상")
    private String optionType;

    @Schema(description = "선택한 옵션 값", example = "내추럴 우드")
    private String optionValue;

    @Schema(description = "옵션 추가 금액", example = "5000")
    private BigDecimal optionAdditionalPrice;

    @Schema(description = "재고", example = "100")
    private Integer optionStock;

    @Schema(description = "옵션 품절 여부", example = "true,false")
    private boolean optionSoldOutYn;
}
