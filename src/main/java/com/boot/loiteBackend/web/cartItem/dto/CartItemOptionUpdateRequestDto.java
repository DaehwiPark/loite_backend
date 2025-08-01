package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemOptionUpdateRequestDto {

    @Schema(description = "변경할 상품 옵션 ID", example = "12")
    private Long productOptionId;

    @Schema(description = "수량", example = "2")
    private Integer quantity;
}
