package com.boot.loiteBackend.web.review.repository;

import com.boot.loiteBackend.web.review.entity.ReviewEntity;
import com.boot.loiteBackend.web.review.entity.ReviewMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewMediaRepository extends JpaRepository<ReviewMediaEntity, Long> {

    // 특정 리뷰에 속한 미디어 전부 조회
    List<ReviewMediaEntity> findByReview_ReviewId(Long reviewId);

    List<ReviewMediaEntity> findByReview(ReviewEntity review);

    int countByReview(ReviewEntity review);

    List<ReviewMediaEntity> findByReviewOrderByCreatedAtAsc(ReviewEntity review);
}
