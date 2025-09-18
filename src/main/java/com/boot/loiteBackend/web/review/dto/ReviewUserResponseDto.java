package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "내 리뷰 조회 응답 DTO")
public class ReviewUserResponseDto {

    @Schema(description = "리뷰 고유 ID", example = "1")
    private Long reviewId;

    @Schema(description = "상품 ID", example = "101")
    private Long productId;

    @Schema(description = "상품명", example = "스마트 전기포트")
    private String productName;

    @Schema(description = "상품 대표 이미지 URL", example = "/uploads/product/image/kettle_main.png")
    private String productImageUrl;

    @Schema(description = "리뷰 별점 (1~5)", example = "5")
    private int rating;

    @Schema(description = "도움되요 수", example = "5")
    private int helpfulCount;

    @Schema(description = "리뷰 본문 내용", example = "배송 빠르고 포장도 깔끔합니다.")
    private String content;

    @Schema(description = "작성자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "작성일시", example = "2025-09-15T22:30:12")
    private LocalDateTime createdAt;

    @Schema(description = "리뷰 첨부 미디어 목록 (이미지/동영상)")
    private List<ReviewMediaDto> medias;

    @Schema(description = "구매한 상품 옵션/사은품/추가구성품 정보")
    private List<ReviewOrderItemDto> orderItems;
}
