package com.boot.loiteBackend.web.cartItem.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CartItemDeleteRequestDto {
    private List<Long> cartItemIds;
}