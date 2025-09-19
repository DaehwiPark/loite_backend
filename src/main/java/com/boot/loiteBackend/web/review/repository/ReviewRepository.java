package com.boot.loiteBackend.web.review.repository;

import com.boot.loiteBackend.web.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 특정 상품 리뷰 목록 (페이지네이션)
    Page<ReviewEntity> findByProduct_ProductIdAndDeleteYn(Long productId, String deleteYn, Pageable pageable);

    // 특정 유저가 작성한 리뷰 목록 (마이페이지)
    Page<ReviewEntity> findByUser_UserIdAndDeleteYn(Long userId, String deleteYn, Pageable pageable);

    // 주문 아이템 단위 리뷰 중복 체크 (주문 아이템 1건당 리뷰 1개)
    boolean existsByOrderItemIdAndDeleteYn(Long orderItemId, String deleteYn);

    // 상품 평균 평점
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM ReviewEntity r " +
            "WHERE r.product.productId = :productId AND r.deleteYn = 'N'")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    // 상품 리뷰 개수
    @Query("SELECT COUNT(r) FROM ReviewEntity r " +
            "WHERE r.product.productId = :productId AND r.deleteYn = 'N'")
    Long countByProductIdAndDeleteYn(@Param("productId") Long productId);

    @Query("SELECT r.rating, COUNT(r) " +
            "FROM ReviewEntity r " +
            "WHERE r.product.productId = :productId " +
            "AND r.deleteYn = 'N' " +
            "GROUP BY r.rating")
    List<Object[]> countReviewsByRating(@Param("productId") Long productId);

    // 포토 상품평 (IMAGE 첨부된 리뷰만)
    @Query("SELECT DISTINCT r FROM ReviewEntity r " +
            "JOIN r.medias m " +
            "WHERE r.product.productId = :productId " +
            "AND r.deleteYn = 'N' " +
            "AND m.mediaType = 'IMAGE'")
    Page<ReviewEntity> findPhotoReviews(@Param("productId") Long productId, Pageable pageable);

    // 동영상 상품평 (VIDEO 첨부된 리뷰만)
    @Query("SELECT DISTINCT r FROM ReviewEntity r " +
            "JOIN r.medias m " +
            "WHERE r.product.productId = :productId " +
            "AND r.deleteYn = 'N' " +
            "AND m.mediaType = 'VIDEO'")
    Page<ReviewEntity> findVideoReviews(@Param("productId") Long productId, Pageable pageable);

    // 일반 상품평 (첨부파일 없는 리뷰만)
    @Query("SELECT r FROM ReviewEntity r " +
            "WHERE r.product.productId = :productId " +
            "AND r.deleteYn = 'N' " +
            "AND NOT EXISTS (SELECT 1 FROM ReviewMediaEntity m WHERE m.review = r)")
    Page<ReviewEntity> findTextOnlyReviews(@Param("productId") Long productId, Pageable pageable);

}


