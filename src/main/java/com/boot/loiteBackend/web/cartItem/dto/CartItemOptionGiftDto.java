package com.boot.loiteBackend.web.cartItem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemOptionGiftDto {
    private Long productOptionId;
    private Long giftId;
    private Integer quantity;
}