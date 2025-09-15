package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "상품 리뷰 요약 정보 DTO")
public class ReviewSummaryDto {

    @Schema(description = "상품 ID", example = "101")
    private Long productId;

    @Schema(description = "리뷰 평균 평점 (1~5)", example = "4.5")
    private Double averageRating;

    @Schema(description = "리뷰 개수", example = "12")
    private Long reviewCount;
}

