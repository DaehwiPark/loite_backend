package com.boot.loiteBackend.web.support.faq.category.dto;

import com.boot.loiteBackend.web.support.faq.category.entity.SupportFaqMajorCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "FAQ 대분류 카테고리 DTO")
public class SupportFaqMajorCategoryDto {

    @Schema(description = "FAQ 대분류 카테고리 ID", example = "1")
    private Long faqMajorCategoryId;

    @Schema(description = "FAQ 대분류 카테고리 이름", example = "회원")
    private String faqMajorCategoryName;

    @Schema(description = "FAQ 대분류 카테고리 정렬 순서", example = "1")
    private Integer faqMajorCategoryOrder;

    public static SupportFaqMajorCategoryDto from(SupportFaqMajorCategoryEntity entity) {
        return SupportFaqMajorCategoryDto.builder()
                .faqMajorCategoryId(entity.getFaqMajorCategoryId())
                .faqMajorCategoryName(entity.getFaqMajorCategoryName())
                .faqMajorCategoryOrder(entity.getFaqMajorCategoryOrder())
                .build();
    }
}