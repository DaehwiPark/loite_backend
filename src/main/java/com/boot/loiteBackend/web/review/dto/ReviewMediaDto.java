package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "리뷰 미디어 DTO")
public class ReviewMediaDto {

    @Schema(description = "미디어 타입", example = "IMAGE")
    private String mediaType;

    @Schema(description = "미디어 URL", example = "/assets/review/image123.jpg")
    private String url;

    @Schema(description = "정렬 순서", example = "1")
    private int sortOrder;
}
