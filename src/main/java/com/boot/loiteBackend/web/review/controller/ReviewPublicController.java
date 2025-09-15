package com.boot.loiteBackend.web.review.controller;

import com.boot.loiteBackend.web.review.dto.ReviewResponseDto;
import com.boot.loiteBackend.web.review.dto.ReviewSummaryDto;
import com.boot.loiteBackend.web.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/reviews")
@RequiredArgsConstructor
@Tag(name = "리뷰(public) API", description = "리뷰 관리(public) API")
public class ReviewPublicController {

    private final ReviewService reviewService;

    @Operation(summary = "상품별 리뷰 목록 조회", description = "특정 상품에 대한 리뷰 목록을 조회합니다. (삭제되지 않은 리뷰만)")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewResponseDto>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준 (베스트순, 최신순, 평점 높은순, 평점 낮은순)", example = "베스트순")
            @RequestParam(defaultValue = "베스트순") String sortType //베스트순, 최신순, 평점 높은순, 평점 낮은순
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> response = reviewService.getReviewsByProduct(productId, sortType, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 리뷰 요약 조회", description = "상품의 리뷰 평균 평점과 개수를 조회합니다.")
    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<ReviewSummaryDto> getReviewSummary(@PathVariable Long productId) {
        ReviewSummaryDto response = reviewService.getReviewSummary(productId);
        return ResponseEntity.ok(response);
    }

}
