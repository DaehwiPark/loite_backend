package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemGiftUpdateRequestDto {

    private List<CartItemGiftUpdateDto> gifts;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemGiftUpdateDto {
        @Schema(description = "사은품 ID", example = "5")
        private Long productGiftId;

        @Schema(description = "수량", example = "2")
        private Integer quantity;
    }
}
