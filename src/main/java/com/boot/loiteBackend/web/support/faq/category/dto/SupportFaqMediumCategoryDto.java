package com.boot.loiteBackend.web.support.faq.category.dto;

import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "FAQ 중분류 카테고리 DTO")
public class SupportFaqMediumCategoryDto {

    @Schema(description = "FAQ 중분류 카테고리 ID", example = "101")
    private Long faqMediumCategoryId;

    @Schema(description = "FAQ 중분류 카테고리 이름", example = "로그인/비밀번호")
    private String faqMediumCategoryName;

    @Schema(description = "FAQ 중분류 카테고리 정렬 순서", example = "1")
    private Integer faqMediumCategoryOrder;

    @Schema(description = "연결된 대분류 카테고리 ID", example = "1")
    private Long faqMajorCategoryId;

    @Schema(description = "FAQ 중분류 카테고리 이미지 URL", example = "/files/faq-category/payment.png")
    private String faqImageUrl;

    public static SupportFaqMediumCategoryDto from(SupportFaqMediumCategoryEntity entity) {
        return SupportFaqMediumCategoryDto.builder()
                .faqMediumCategoryId(entity.getFaqMediumCategoryId())
                .faqMediumCategoryName(entity.getFaqMediumCategoryName())
                .faqMediumCategoryOrder(entity.getFaqMediumCategoryOrder())
                .faqMajorCategoryId(entity.getFaqMajorCategory().getFaqMajorCategoryId())
                .faqImageUrl(entity.getFaqImageUrl())
                .build();
    }
}