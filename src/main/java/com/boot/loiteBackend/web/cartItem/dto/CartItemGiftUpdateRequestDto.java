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

    @Schema(description = "카트아이템 ID", example = "10")
    private Long cartItemId;

    @Schema(description = "선택한 사은품 리스트")
    private List<GiftItem> giftList;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GiftItem {
        @Schema(description = "사은품 ID", example = "5")
        private Long productGiftId;

        @Schema(description = "수량", example = "2")
        private Integer quantity;
    }
}
