package com.boot.loiteBackend.web.wishlist.repository;

import com.boot.loiteBackend.web.wishlist.entity.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {

    Optional<WishlistEntity> findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    List<WishlistEntity> findByUser_UserId(Long userId);
}
