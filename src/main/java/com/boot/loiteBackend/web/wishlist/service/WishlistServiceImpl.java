package com.boot.loiteBackend.web.wishlist.service;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import com.boot.loiteBackend.web.user.general.repository.UserRepository;
import com.boot.loiteBackend.web.wishlist.dto.WishlistDto;
import com.boot.loiteBackend.web.wishlist.entity.WishlistEntity;
import com.boot.loiteBackend.web.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final AdminProductRepository adminProductRepository;

    @Override
    public boolean toggleWishlist(Long userId, Long productId) {
        Optional<WishlistEntity> existing =
                wishlistRepository.findByUser_UserIdAndProduct_ProductId(userId, productId);

        if (existing.isPresent()) {
            // 이미 있으면 삭제 → 찜 취소
            wishlistRepository.delete(existing.get());
            return false;
        } else {
            // 없으면 추가 → 찜 등록
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

            AdminProductEntity product = adminProductRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

            WishlistEntity wishlist = WishlistEntity.builder()
                    .user(user)
                    .product(product)
                    .build();

            wishlistRepository.save(wishlist);
            return true;
        }
    }

    @Override
    public List<WishlistDto> getMyWishlist(Long userId) {
        List<WishlistEntity> wishlists = wishlistRepository.findByUser_UserId(userId);

        return wishlists.stream()
                .map(w -> {
                    var product = w.getProduct();

                    String imageUrl = product.getProductImages().stream()
                            .filter(img -> "Y".equals(img.getActiveYn()))
                            .sorted(Comparator.comparingInt(AdminProductImageEntity::getImageSortOrder))
                            .map(AdminProductImageEntity::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    return WishlistDto.builder()
                            .wishlistId(w.getWishlistId())
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .productImageUrl(imageUrl)
                            .originalPrice(product.getProductPrice())
                            .discountRate(product.getDiscountRate())
                            .discountedPrice(product.getDiscountedPrice())
                            .createdAt(w.getCreatedAt())
                            .build();
                })
                .toList();
    }
}
