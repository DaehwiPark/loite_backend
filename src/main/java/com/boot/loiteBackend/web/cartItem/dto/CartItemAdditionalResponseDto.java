package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemAdditionalResponseDto {

    @Schema(description = "장바구니 ID", example = "1")
    private Long cartItemId;

    @Schema(description = "추가구성품 ID", example = "1")
    private Long productAdditionalId;

    @Schema(description = "선택한 추가구성품명", example = "포켓몬빵")
    private String additionalName;

    @Schema(description = "추가구성품 이미지 URL", example = "https://cdn.loite.com/images/additional/rug.jpg")
    private String additionalImageUrl;

    @Schema(description = "추가구성품 재고", example = "50")
    private Integer additionalStock;

    @Schema(description = "추가구성품 수량", example = "2")
    private Integer quantity;

    @Schema(description = "추가구성품 품절 여부", example = "true,false")
    private boolean additionalSoldOutYn;
}
