package com.boot.loiteBackend.web.cartItem.controller;

import com.boot.loiteBackend.web.cartItem.dto.CartItemDeleteRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemResponseDto;
import com.boot.loiteBackend.web.cartItem.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/cart-items")
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

    @PatchMapping("/{cartItemId}/checked")//?checked=false or true
    public ResponseEntity<Void> updateCheckedYn(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long cartItemId, @RequestParam boolean checked) {
        cartItemService.updateCheckedYn(userId, cartItemId, checked);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/selectDelete") /*@AuthenticationPrincipal*/
    public ResponseEntity<Void> deleteCartItems(@RequestHeader("X-USER-ID") Long userId, @RequestBody CartItemDeleteRequestDto requestDto) {
        cartItemService.deleteCartItems(userId, requestDto.getCartItemIds());
        return ResponseEntity.noContent().build(); // 204 응답
    }

    @DeleteMapping("/checkedDelete")
    public ResponseEntity<Void> deleteCheckedCartItems(@RequestHeader("X-USER-ID") Long userId) {
        cartItemService.deleteCheckedCartItems(userId);
        return ResponseEntity.noContent().build(); // 204 응답
    }
}
