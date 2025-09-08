package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemAdditionalDto {

    @Schema(description = "상품-추가구성품 매핑 ID", example = "1")
    private Long productAdditionalId;

    @Schema(description = "추가구성품 수량", example = "1")
    private Integer quantity;
}
