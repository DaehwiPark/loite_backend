package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.web.cartItem.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemService {

    @Transactional
    void addToCart(Long loginUserId, List<CartItemRequestDto> requestList);

    List<CartItemResponseDto> getCartItemsByUser(Long loginUserId);

    void updateCheckedYn(Long loginUserId, Long cartItemId, boolean checked);

    void deleteCartItems(Long loginUserId, List<Long> cartItemIds);

    void updateCartItemOption(Long loginUserId, Long cartItemId, CartItemOptionUpdateRequestDto requestDto);

    void updateCartItemQuantity(Long loginUserId, Long cartItemId, CartItemQuantityUpdateRequestDto requestDto);

    @Transactional(readOnly = true)
    List<AvailableOptionResponseDto> getAvailableOptions(Long cartItemId);

    List<AvailableGiftResponseDto> getAvailableGifts(Long cartItemId);

    void updateCartItemGifts(Long loginUserId, Long cartItemId, CartItemGiftUpdateRequestDto dto);

}