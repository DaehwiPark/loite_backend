package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemGiftResponseDto {

    @Schema(description = "장바구니 ID", example = "1")
    private Long cartItemId;

    @Schema(description = "사은품 ID", example = "1")
    private Long productGiftId;

    @Schema(description = "선택한 사은품명", example = "포켓몬빵")
    private String giftName;

    @Schema(description = "사은품 이미지 URL", example = "https://cdn.loite.com/images/gift/rug.jpg")
    private String giftImageUrl;

    @Schema(description = "사은품 수량", example = "2")
    private Integer quantity;
}
