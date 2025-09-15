package com.boot.loiteBackend.web.wishlist.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.wishlist.dto.WishlistDto;
import com.boot.loiteBackend.web.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/private/wishlist")
@RequiredArgsConstructor
@Tag(name = "위시리스트 API", description = "로그인한 사용자의 위시리스트 관리 API")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "상품 찜 토글", description = "상품을 찜하거나 찜을 취소합니다. (로그인 필요)")
    @PostMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> toggleWishlist(@AuthenticationPrincipal CustomUserDetails loginUser, @PathVariable Long productId) {
        boolean added = wishlistService.toggleWishlist(loginUser.getUserId(), productId);
        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("wishlistAdded", added);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 위시리스트 조회", description = "로그인한 사용자가 찜한 상품 목록을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<WishlistDto>> getMyWishlist(@AuthenticationPrincipal CustomUserDetails loginUser) {
        List<WishlistDto> response = wishlistService.getMyWishlist(loginUser.getUserId());

        return ResponseEntity.ok(response);
    }
}

