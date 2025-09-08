package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDto {
    @Schema(description = "추가할 상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "옵션 ID", example = "1")
    private Long productOptionId;

    @Schema(description = "수량", example = "1")
    private Integer quantity;

    private List<CartItemGiftDto> gifts;

    private List<CartItemAdditionalDto> additionals;

}

