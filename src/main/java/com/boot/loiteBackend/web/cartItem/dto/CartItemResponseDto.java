package com.boot.loiteBackend.web.cartItem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private String brandName;
    private String thumbnailUrl;
    private String optionText;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;
    private boolean checked;
}

