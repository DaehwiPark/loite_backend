package com.boot.loiteBackend.web.review.service;

import com.boot.loiteBackend.web.review.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {

    ReviewResponseDto createReview(Long userId, ReviewRequestDto requestDto, List<MultipartFile> files);

    ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewUpdateRequestDto requestDto, List<MultipartFile> files);

    Page<ReviewUserResponseDto> getMyReviews(Long userId, Pageable pageable);

    void deleteReview(Long userId, Long reviewId);

    Page<ReviewResponseDto> getReviewsByProduct(Long productId,String sortType, Pageable pageable);

    boolean toggleHelpful(Long userId, Long reviewId);

    ReviewSummaryDto getReviewSummary(Long productId);
}
