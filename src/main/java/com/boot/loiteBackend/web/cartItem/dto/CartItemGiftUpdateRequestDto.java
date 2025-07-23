package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemGiftUpdateRequestDto {

    @Schema(description = "선택한 사은품 ID", example = "5")
    private Long giftId;
}
