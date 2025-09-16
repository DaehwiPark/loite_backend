package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class ReviewRatingStatsDto {

    @Schema(description = "별점별 개수 (key = rating, value = count)")
    private Map<Integer, Long> ratingCounts;

    @Schema(description = "별점별 비율 (key = rating, value = %)")
    private Map<Integer, Integer> ratingPercents;

    @Schema(description = "리뷰 총 개수")
    private Long totalCount;
}

