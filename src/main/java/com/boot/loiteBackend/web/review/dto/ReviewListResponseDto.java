package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListResponseDto {

    @Schema(description = "리뷰 목록")
    private Page<ReviewResponseDto> reviews;

    @Schema(description = "별점 통계")
    private ReviewRatingStatsDto stats;
}

