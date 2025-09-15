package com.boot.loiteBackend.web.review.repository;

import com.boot.loiteBackend.web.review.entity.ReviewHelpfulEntity;
import com.boot.loiteBackend.web.review.entity.ReviewMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewHelpfulRepository extends JpaRepository<ReviewHelpfulEntity, Long> {
    Optional<ReviewHelpfulEntity> findByReview_ReviewIdAndUser_UserId(Long reviewId, Long userId);
}
