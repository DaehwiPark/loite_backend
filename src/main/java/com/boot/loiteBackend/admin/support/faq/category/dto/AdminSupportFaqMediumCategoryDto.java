package com.boot.loiteBackend.admin.support.faq.category.dto;

import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqMediumCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(description = "FAQ 중분류 카테고리 응답 DTO")
@Data
@Builder
@AllArgsConstructor
public class AdminSupportFaqMediumCategoryDto {

    @Schema(description = "중분류 카테고리 ID", example = "1")
    private Long faqMediumCategoryId;

    @Schema(description = "중분류 카테고리 이름", example = "결제 관련")
    private String faqMediumCategoryName;

    @Schema(description = "중분류 정렬 순서", example = "1")
    private Integer faqMediumCategoryOrder;

    @Schema(description = "대분류 ID", example = "1")
    private Long faqMajorCategoryId;

    @Schema(description = "대분류 이름", example = "회원 서비스")
    private String faqMajorCategoryName;

    public AdminSupportFaqMediumCategoryDto(AdminSupportFaqMediumCategoryEntity entity) {
        this.faqMediumCategoryId = entity.getFaqMediumCategoryId();
        this.faqMediumCategoryName = entity.getFaqMediumCategoryName();
        this.faqMediumCategoryOrder = entity.getFaqMediumCategoryOrder();
        this.faqMajorCategoryId = entity.getFaqMajorCategory().getFaqMajorCategoryId();
        this.faqMajorCategoryName = entity.getFaqMajorCategory().getFaqMajorCategoryName();
    }

}
