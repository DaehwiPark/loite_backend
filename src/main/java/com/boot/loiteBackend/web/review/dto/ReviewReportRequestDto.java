package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReportRequestDto {

    @Schema(description = "신고 대상 리뷰 ID", example = "10")
    private Long reviewId;

    @Schema(description = "신고 사유", example = "욕설/비방 포함")
    private String reason;
}
