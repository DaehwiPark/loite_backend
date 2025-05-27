package com.boot.loiteMsBack.support.faq.dto;

import com.boot.loiteMsBack.support.faq.entity.SupportFaqCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "FAQ 카테고리 응답 DTO")
@Getter
@Setter
public class SupportFaqCategoryDto {

    @Schema(description = "카테고리 ID", example = "1")
    private Long faqCategoryId;

    @Schema(description = "카테고리 이름", example = "계정 관련")
    private String faqCategoryName;

    @Schema(description = "카테고리 정렬 순서", example = "1")
    private Integer faqCategoryOrder;

    public SupportFaqCategoryDto(SupportFaqCategoryEntity entity) {
        this.faqCategoryId = entity.getFaqCategoryId();
        this.faqCategoryName = entity.getFaqCategoryName();
        this.faqCategoryOrder = entity.getFaqCategoryOrder();
    }
}
