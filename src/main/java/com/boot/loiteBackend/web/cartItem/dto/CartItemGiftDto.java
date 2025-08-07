package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemGiftDto {

    @Schema(description = "상품-사은품 매핑 ID", example = "34")
    private Long productGiftId;

    @Schema(description = "사은품 수량", example = "1")
    private Integer quantity;
}
