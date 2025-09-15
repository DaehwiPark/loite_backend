package com.boot.loiteBackend.web.review.repository;

import com.boot.loiteBackend.web.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 특정 상품 리뷰 목록 (페이지네이션)
    Page<ReviewEntity> findByProduct_ProductIdAndDeleteYn(Long productId, String deleteYn, Pageable pageable);

    // 특정 유저가 작성한 리뷰 목록 (마이페이지)
    Page<ReviewEntity> findByUser_UserIdAndDeleteYn(Long userId, String deleteYn, Pageable pageable);

    // 주문 단위 리뷰 중복 체크 (주문 1건당 리뷰 1개)
    boolean existsByOrderIdAndDeleteYn(Long orderId, String deleteYn);

    // 상품 평균 평점
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM ReviewEntity r " +
            "WHERE r.product.productId = :productId AND r.deleteYn = 'N'")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    // 상품 리뷰 개수
    @Query("SELECT COUNT(r) FROM ReviewEntity r " +
            "WHERE r.product.productId = :productId AND r.deleteYn = 'N'")
    Long countByProductIdAndDeleteYn(@Param("productId") Long productId);
}


