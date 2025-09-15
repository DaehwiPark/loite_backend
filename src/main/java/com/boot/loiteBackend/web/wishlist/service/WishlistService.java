package com.boot.loiteBackend.web.wishlist.service;

import com.boot.loiteBackend.web.wishlist.dto.WishlistDto;

import java.util.List;

public interface WishlistService {

    boolean toggleWishlist(Long userId, Long productId);

    List<WishlistDto> getMyWishlist(Long userId);
}
