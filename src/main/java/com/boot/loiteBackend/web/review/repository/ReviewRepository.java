package com.boot.loiteBackend.web.review.repository;

import com.boot.loiteBackend.web.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Page<ReviewEntity> findByProduct_ProductId(Long productId, Pageable pageable);

    Optional<ReviewEntity> findByProduct_ProductIdAndUser_UserId(Long productId, Long userId);

    boolean existsByUser_UserIdAndProduct_ProductIdAndOrderId(Long userId, Long productId, Long orderId);

    Page<ReviewEntity> findByUser_UserIdAndDeleteYn(Long userId, String deleteYn, Pageable pageable);

    Page<ReviewEntity> findByProduct_ProductIdAndDeleteYn(Long productId, String deleteYn, Pageable pageable);
}

