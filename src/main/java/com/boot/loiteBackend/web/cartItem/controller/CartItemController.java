package com.boot.loiteBackend.web.cartItem.controller;

import com.boot.loiteBackend.web.cartItem.dto.*;
import com.boot.loiteBackend.web.cartItem.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/cart-items")
@Tag(name = "장바구니 ", description = "장바구니 관련 기능 API")
public class CartItemController {
    private final CartItemService cartItemService;

    @Operation(summary = "장바구니 추가", description = "상품을 장바구니에 추가 합니다.")
    @PostMapping
    public ResponseEntity<Void> addCartItem(@RequestBody CartItemRequestDto requestDto, @RequestHeader("X-USER-ID") Long userId) {
        cartItemService.addToCart(userId, requestDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 조회", description = "장바구니에 추가된 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(@RequestHeader("X-USER-ID") Long userId) {
        List<CartItemResponseDto> items = cartItemService.getCartItemsByUser(userId);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "상품 선택 상태 수정", description = "장바구니에 담긴 상품의 선택 상태를 수정합니다.")
    @PatchMapping("/{cartItemId}/checked")//?checked=false or true
    public ResponseEntity<Void> updateCheckedYn(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long cartItemId, @RequestParam boolean checked) {
        cartItemService.updateCheckedYn(userId, cartItemId, checked);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 단일 삭제", description = "장바구니에 추가된 상품을 골라 삭제합니다.")
    @DeleteMapping("/selectDelete") /*@AuthenticationPrincipal*/
    public ResponseEntity<Void> deleteCartItems(@RequestHeader("X-USER-ID") Long userId, @RequestBody CartItemDeleteRequestDto requestDto) {
        cartItemService.deleteCartItems(userId, requestDto.getCartItemIds());
        return ResponseEntity.noContent().build(); // 204 응답
    }

    @Operation(summary = "장바구니 상품 중복 삭제", description = "장바구니에 추가된 상품을 선택하여 중복 삭제합니다.")
    @DeleteMapping("/checkedDelete")
    public ResponseEntity<Void> deleteCheckedCartItems(@RequestHeader("X-USER-ID") Long userId) {
        cartItemService.deleteCheckedCartItems(userId);
        return ResponseEntity.noContent().build(); // 204 응답
    }

    @Operation(summary = "장바구니 상품 옵션 변경", description = "장바구니에 추가된 상품의 옵션을 변경합니다.")
    @PutMapping("/{cartItemId}/updateOption")
    public ResponseEntity<Void> updateCartItemOption(@PathVariable Long cartItemId, @RequestBody CartItemOptionUpdateRequestDto requestDto, @RequestHeader("X-USER-ID") Long userId) {
        cartItemService.updateCartItemOption(userId, cartItemId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 사은품 변경", description = "장바구니에 추가된 상품의 사은품을 변경합니다.")
    @PutMapping("/{cartItemId}/updateGift")
    public ResponseEntity<Void> updateCartItemGift(@PathVariable Long cartItemId, @RequestBody CartItemGiftUpdateRequestDto requestDto, @RequestHeader("X-USER-ID") Long userId) {
        cartItemService.updateCartItemGift(userId, cartItemId, requestDto);
        return ResponseEntity.ok().build();
    }
}
