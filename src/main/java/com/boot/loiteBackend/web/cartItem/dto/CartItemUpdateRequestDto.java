package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemUpdateRequestDto {

    @Schema(description = "상품 수량", example = "2")
    private Integer quantity;

    @Schema(description = "옵션 목록")
    private List<CartItemOptionUpdateDto> options;

    @Schema(description = "사은품 목록")
    private List<CartItemGiftUpdateDto> gifts;

    @Schema(description = "추가구성품 목록")
    private List<CartItemAdditionalUpdateDto> additionals;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItemOptionUpdateDto {

        @Schema(description = "상품 옵션 ID", example = "12")
        private Long productOptionId;

        @Schema(description = "옵션별 수량", example = "1")
        private Integer quantity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItemGiftUpdateDto {
        @Schema(description = "사은품 ID", example = "5")
        private Long productGiftId;

        @Schema(description = "수량", example = "2")
        private Integer quantity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItemAdditionalUpdateDto {
        @Schema(description = "추가구성품 매핑 ID", example = "3")
        private Long productAdditionalId;

        @Schema(description = "수량", example = "1")
        private Integer quantity;
    }
}
