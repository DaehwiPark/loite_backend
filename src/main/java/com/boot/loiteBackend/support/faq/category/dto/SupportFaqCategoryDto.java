package com.boot.loiteBackend.support.faq.category.dto;

import com.boot.loiteBackend.support.faq.category.entity.SupportFaqCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "FAQ 카테고리 응답 DTO")
@Data
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
