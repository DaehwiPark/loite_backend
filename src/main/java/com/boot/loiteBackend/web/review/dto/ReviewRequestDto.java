package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "리뷰 등록 요청 DTO")
public class ReviewRequestDto {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "주문 ID (선택)", example = "160")
    private Long orderId;

    @Schema(description = "리뷰 별점 (1~5)", example = "5")
    private int rating;

    @Schema(description = "리뷰 내용", example = "배송 빨랐고 상품 상태도 좋습니다!")
    private String content;

    /*@Schema(description = "첨부 파일 (이미지/동영상)", type = "array", format = "binary")
    private List<MultipartFile> files;*/
}

