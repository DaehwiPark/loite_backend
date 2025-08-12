package com.boot.loiteBackend.web.cartItem.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
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
    public ResponseEntity<Void> addCartItems(@RequestBody List<CartItemRequestDto> requestList,
                                             @AuthenticationPrincipal CustomUserDetails loginUser) {
        cartItemService.addToCart(loginUser.getUserId(), requestList);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "장바구니 조회", description = "장바구니에 추가된 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(@AuthenticationPrincipal CustomUserDetails loginUser) {
        List<CartItemResponseDto> items = cartItemService.getCartItemsByUser(loginUser.getUserId());
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "상품 선택 상태 수정", description = "장바구니에 담긴 상품의 선택 상태를 수정합니다.")
    @PatchMapping("/{cartItemId}/checked")//?checked=false or true
    public ResponseEntity<Void> updateCheckedYn(@PathVariable Long cartItemId, @RequestParam boolean checked, @AuthenticationPrincipal CustomUserDetails loginUser) {
        cartItemService.updateCheckedYn(loginUser.getUserId(), cartItemId, checked);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 단일 삭제", description = "장바구니에 추가된 상품을 골라 삭제합니다.")
    @DeleteMapping("/selectDelete") /*@AuthenticationPrincipal*/
    public ResponseEntity<Void> deleteCartItems(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody CartItemDeleteRequestDto requestDto) {
        cartItemService.deleteCartItems(loginUser.getUserId(), requestDto.getCartItemIds());
        return ResponseEntity.noContent().build(); // 204 응답
    }

    @Operation(summary = "장바구니 상품 중복 삭제", description = "장바구니에 추가된 상품을 선택하여 중복 삭제합니다.")
    @DeleteMapping("/checkedDelete")
    public ResponseEntity<Void> deleteCheckedCartItems(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody CartItemDeleteRequestDto requestDto) {
        cartItemService.deleteCartItems(loginUser.getUserId(), requestDto.getCartItemIds());
        return ResponseEntity.noContent().build(); // 204 응답
    }

    @Operation(summary = "장바구니 상품 옵션 및 수량 변경", description = "장바구니에 추가된 상품의 옵션과 수량을 변경합니다.")
    @PutMapping("/{cartItemId}/updateOption")
    public ResponseEntity<Void> updateCartItemOption(@PathVariable Long cartItemId, @RequestBody CartItemOptionUpdateRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails loginUser) {
        cartItemService.updateCartItemOption(loginUser.getUserId(), cartItemId, requestDto);
        return ResponseEntity.ok().build();
    }

    /*@Operation(summary = "장바구니 상품 사은품 변경", description = "장바구니에 추가된 상품의 사은품을 변경합니다.")
    @PutMapping("/{cartItemId}/updateGift") 
    public ResponseEntity<Void> updateCartItemGifts(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @PathVariable Long cartItemId,
            @RequestBody List<CartItemGiftUpdateRequestDto.GiftItem> requestDtoList
    ){
        cartItemService.updateCartItemGifts(loginUser.getUserId(), cartItemId, requestDtoList);
        return ResponseEntity.ok().build();
    }*/

    @Operation(summary = "장바구니 상품 수량 변경", description = "장바구니에 추가된 상품의 수량을 변경합니다.")
    @PatchMapping("/{cartItemId}/updateProductQuantity")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable Long cartItemId, @RequestBody CartItemQuantityUpdateRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails loginUser) {
        cartItemService.updateCartItemQuantity(loginUser.getUserId(), cartItemId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품에 연결된 사은품 목록 조회", description = "장바구니에 추가된 상품과 연결된 사은품을 조회합니다.")
    @GetMapping("/{cartItemId}/availableGifts")
    public ResponseEntity<List<AvailableGiftResponseDto>> getAvailableGifts(@PathVariable Long cartItemId) {
        List<AvailableGiftResponseDto> gifts = cartItemService.getAvailableGifts(cartItemId);
        return ResponseEntity.ok(gifts);
    }

    @Operation(summary = "상품에 연결된 옵션 목록 조회", description = "장바구니에 추가된 상품의 옵션을 조회합니다.")
    @GetMapping("/{cartItemId}/options")
    public ResponseEntity<List<AvailableOptionResponseDto>> getAvailableOptions(@PathVariable Long cartItemId) {
        List<AvailableOptionResponseDto> options = cartItemService.getAvailableOptions(cartItemId);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "장바구니 상품 사은품 변경", description = "장바구니 상품에 연결된 사은품 목록을 수정합니다.")
    @PutMapping("/{cartItemId}/updateGifts")
    public ResponseEntity<Void> updateCartItemGifts(@PathVariable Long cartItemId, @RequestBody CartItemGiftUpdateRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails loginUser) {
        cartItemService.updateCartItemGifts(loginUser.getUserId(), cartItemId, requestDto);
        return ResponseEntity.ok().build();
    }
}
