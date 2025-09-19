package com.boot.loiteBackend.web.review.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.review.dto.*;
import com.boot.loiteBackend.web.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/private/reviews")
@RequiredArgsConstructor
@Tag(name = "리뷰(private) API", description = "리뷰 관리(private) API")
public class ReviewPrivateController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "사용자가 구매한 상품의 리뷰를 작성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewResponseDto> createReview(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails loginUser, @ModelAttribute ReviewRequestDto requestDto, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        ReviewResponseDto response = reviewService.createReview(loginUser.getUserId(), requestDto, files);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 수정", description = "사용자가 본인 리뷰를 수정합니다.")
    @PutMapping(value = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewResponseDto> updateReview(@AuthenticationPrincipal CustomUserDetails loginUser, @PathVariable Long reviewId, @ModelAttribute ReviewUpdateRequestDto requestDto, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        ReviewResponseDto response = reviewService.updateReview(loginUser.getUserId(), reviewId, requestDto, files);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 삭제", description = "사용자가 작성한 리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal CustomUserDetails loginUser, @PathVariable Long reviewId) {
        reviewService.deleteReview(loginUser.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내가 작성한 리뷰 조회", description = "사용자가 작성한 리뷰 전체를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<Page<ReviewUserResponseDto>> getMyReviews(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReviewUserResponseDto> response = reviewService.getMyReviews(loginUser.getUserId(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 도움돼요 토글", description = "리뷰 도움돼요를 누르거나 취소합니다.")
    @PostMapping("/{reviewId}/helpful")
    public ResponseEntity<Map<String, Object>> toggleHelpful(@AuthenticationPrincipal CustomUserDetails loginUser, @PathVariable Long reviewId) {

        boolean added = reviewService.toggleHelpful(loginUser.getUserId(), reviewId);

        Map<String, Object> response = new HashMap<>();
        response.put("reviewId", reviewId);
        response.put("helpfulAdded", added);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 신고", description = "사용자가 특정 리뷰를 신고합니다.")
    @PostMapping("/report")
    public ResponseEntity<String> reportReview(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestBody ReviewReportRequestDto requestDto
    ) {
        reviewService.reportReview(loginUser.getUserId(), requestDto);
        return ResponseEntity.ok("리뷰가 신고되었습니다.");
    }


}

