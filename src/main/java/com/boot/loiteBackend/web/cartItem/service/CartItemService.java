package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.web.cartItem.dto.*;

import java.util.List;

public interface CartItemService {

    void addToCart(Long loginUserId, CartItemRequestDto requestDto);

    List<CartItemResponseDto> getCartItemsByUser(Long loginUserId);

    void updateCheckedYn(Long loginUserId, Long cartItemId, boolean checked);

    void deleteCartItems(Long loginUserId, List<Long> cartItemIds);

    //void deleteCheckedCartItems(Long userId);

    void updateCartItemOption(Long loginUserId, Long cartItemId, CartItemOptionUpdateRequestDto requestDto);

    //void updateCartItemGift(Long loginUserId, Long cartItemId, CartItemGiftUpdateRequestDto requestDto);

    void updateCartItemQuantity(Long loginUserId, Long cartItemId, CartItemQuantityUpdateRequestDto requestDto);

    void updateCartItemGifts(Long loginUserId, Long cartItemId, List<CartItemGiftUpdateRequestDto.GiftItem> requestDto);

}