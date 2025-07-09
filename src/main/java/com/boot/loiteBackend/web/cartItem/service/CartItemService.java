package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.web.cartItem.dto.CartItemRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemResponseDto;

import java.util.List;

public interface CartItemService {

    void addToCart(Long userId, CartItemRequestDto requestDto);

    List<CartItemResponseDto> getCartItemsByUser(Long userId);

    void updateCheckedYn(Long userId, Long cartItemId, boolean checked);

    void deleteCartItems(Long userId, List<Long> cartItemIds);

    void deleteCheckedCartItems(Long userId);

}