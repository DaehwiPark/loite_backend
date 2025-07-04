package com.boot.loiteBackend.web.cartItem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDto {

    private Long productId;
    private Long productOptionId;
    private String optionText;
    private Integer quantity;
}
