package com.boot.loiteBackend.web.cartItem.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDto {
    private Long productId;
    private List<CartItemOptionGiftDto> items;
}
