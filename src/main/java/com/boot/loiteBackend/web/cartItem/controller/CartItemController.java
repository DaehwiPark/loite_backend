package com.boot.loiteBackend.web.cartItem.controller;

import com.boot.loiteBackend.web.cartItem.dto.CartItemRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemResponseDto;
import com.boot.loiteBackend.web.cartItem.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<Void> addCartItem(@RequestBody CartItemRequestDto requestDto, @RequestHeader("X-USER-ID") Long userId) {
        cartItemService.addToCart(userId, requestDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(@RequestHeader("X-USER-ID") Long userId) {
        List<CartItemResponseDto> items = cartItemService.getCartItemsByUser(userId);
        return ResponseEntity.ok(items);
    }
}
