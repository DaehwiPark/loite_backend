package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemOptionGiftDto {

    @Schema(description = "옵션id", example = "1")
    private Long productOptionId;

    @Schema(description = "수량", example = "1")
    private Integer quantity;

    private List<CartItemGiftDto> gifts;
}