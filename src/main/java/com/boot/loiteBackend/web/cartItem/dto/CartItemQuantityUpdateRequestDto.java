package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemQuantityUpdateRequestDto {

    @Schema(description = "변경 수량", example = "3")
    private int quantity;
}
