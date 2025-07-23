package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDto {

    @Schema(description = "추가할 상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "선택한 상품 옵션 ID", example = "12")
    private Long productOptionId;

    @Schema(description = "선택한 사은품 ID (선택 사항)", example = "5")
    private Long giftId;

    @Schema(description = "주문 수량", example = "2")
    private Integer quantity;
}

