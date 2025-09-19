package com.boot.loiteBackend.web.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "리뷰 등록 응답 DTO")
public class ReviewResponseDto {

    @Schema(description = "리뷰 ID", example = "10001")
    private Long reviewId;

    @Schema(description = "상품 ID", example = "101")
    private Long productId;

    @Schema(description = "상품 이름", example = "name")
    private String productName;

    @Schema(description = "별점", example = "5")
    private int rating;

    @Schema(description = "도움되요 수", example = "5")
    private int helpfulCount;

    @Schema(description = "도움되요 클릭 여부", example = "true")
    private boolean helpfulAdded;

    @Schema(description = "리뷰 내용", example = "아주 만족스러운 제품입니다.")
    private String content;

    @Schema(description = "작성자 닉네임", example = "loite_user")
    private String userName;

    @Schema(description = "리뷰 작성일시", example = "2025-09-15T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "등록된 미디어 리스트")
    private List<ReviewMediaDto> medias;

    @Schema(description = "구매한 상품 옵션/사은품/추가구성품 정보")
    private List<ReviewOrderItemDto> orderItems;
}

