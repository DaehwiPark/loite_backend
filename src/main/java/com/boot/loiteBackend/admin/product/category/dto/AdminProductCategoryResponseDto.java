package com.boot.loiteBackend.admin.product.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductCategoryResponseDto {
    @Schema(description = "카테고리 ID", example = "1, 2, 3 ...")
    private Long categoryId;

    @Schema(description = "상위 카테고리 ID", example = "1, 2, 3 ...")
    private Long ParentId;

    @Schema(description = "카테고리명", example = "생활가전")
    private String categoryName;

    @Schema(description = "카테고리 경로", example = "electronics")
    private String categoryPath;

    @Schema(description = "카테고리 아이콘", example = "example")
    private String categoryIconUrl;

    @Schema(description = "카테고리 이미지", example = "example")
    private String categoryImageUrl;

    @Schema(description = "카테고리 뎁스", example = "가전 = 1, 생활가전 = 2, 건조기 = 3")
    private Integer categoryDepth;

    @Schema(description = "카테고리 정렬 순서", example = "1, 2, 3")
    private int categorySortOrder;

    @Schema(description = "사용 여부", example = "Y, N")
    private String activeYn;

    @Schema(description = "삭제 여부", example = "Y, N")
    private String deleteYn;

    @Schema(description = "생성 일자", example = "2025-08-15")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일자", example = "2025-08-15")
    private LocalDateTime updatedAt;

    @Schema(description = "상위 카테고리 자식들", example = "가전 - 생활가전 - 건조기")
    private List<AdminProductCategoryResponseDto> children = new ArrayList<>();
}
