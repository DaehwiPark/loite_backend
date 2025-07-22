package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.web.cartItem.dto.CartItemGiftUpdateRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemOptionUpdateRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemResponseDto;

import java.util.List;

public interface CartItemService {

    void addToCart(Long loginUserId, CartItemRequestDto requestDto);

    List<CartItemResponseDto> getCartItemsByUser(Long loginUserId);

    void updateCheckedYn(Long loginUserId, Long cartItemId, boolean checked);

    void deleteCartItems(Long loginUserId, List<Long> cartItemIds);

//    void deleteCheckedCartItems(Long userId);

    void updateCartItemOption(Long loginUserId, Long cartItemId, CartItemOptionUpdateRequestDto requestDto);

    void updateCartItemGift(Long loginUserId, Long cartItemId, CartItemGiftUpdateRequestDto requestDto);

}