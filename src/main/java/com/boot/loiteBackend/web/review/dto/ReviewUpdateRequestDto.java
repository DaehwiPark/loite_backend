package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequestDto {

    @Schema(description = "리뷰 별점 (1~5)", example = "1")
    private int rating;

    @Schema(description = "리뷰 본문 내용", example = "다시 생각해보니까 별로에요.")
    private String content;

    @Schema(description = "삭제할 리뷰 미디어 ID 목록")
    private List<Long> deleteMediaIds;
}
