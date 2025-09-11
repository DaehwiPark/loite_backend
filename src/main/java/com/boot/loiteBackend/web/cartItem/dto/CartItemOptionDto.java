package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemOptionDto {

    @Schema(description = "옵션 매핑 ID", example = "34")
    private Long productOptionId;

}
