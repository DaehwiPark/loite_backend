package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.web.cartItem.dto.CartItemGiftUpdateRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemOptionUpdateRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemResponseDto;

import java.util.List;

public interface CartItemService {

    void addToCart(Long userId, CartItemRequestDto requestDto);

    List<CartItemResponseDto> getCartItemsByUser(Long userId);

    void updateCheckedYn(Long userId, Long cartItemId, boolean checked);

    void deleteCartItems(Long userId, List<Long> cartItemIds);

//    void deleteCheckedCartItems(Long userId);

    void updateCartItemOption(Long userId, Long cartItemId, CartItemOptionUpdateRequestDto requestDto);

    void updateCartItemGift(Long userId, Long cartItemId, CartItemGiftUpdateRequestDto requestDto);

}