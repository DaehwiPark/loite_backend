package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDto {
    @Schema(description = "추가할 상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "옵션 및 사은품 배열", example = "items")
    private List<CartItemOptionGiftDto> items;

    /*@Schema(description = "추가할 상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "선택한 상품 옵션 ID", example = "12")
    private Long productOptionId;

    @Schema(description = "선택한 사은품 ID 리스트", example = "[2,4]")
    private List<Long> giftIdList;

    @Schema(description = "주문 수량", example = "2")
    private Integer quantity;*/
}

