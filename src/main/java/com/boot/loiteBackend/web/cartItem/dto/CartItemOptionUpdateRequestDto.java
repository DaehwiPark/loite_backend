package com.boot.loiteBackend.web.cartItem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemOptionUpdateRequestDto {
    private Long productOptionId;
}
