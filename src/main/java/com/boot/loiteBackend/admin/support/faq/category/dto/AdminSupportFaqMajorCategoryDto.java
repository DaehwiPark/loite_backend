package com.boot.loiteBackend.admin.support.faq.category.dto;

import com.boot.loiteBackend.admin.support.faq.category.entity.AdminSupportFaqMajorCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "FAQ 대분류 카테고리 응답 DTO")
@Data
public class AdminSupportFaqMajorCategoryDto {

    @Schema(description = "대분류 카테고리 ID", example = "1")
    private Long faqMajorCategoryId;

    @Schema(description = "대분류 카테고리 이름", example = "회원 서비스")
    private String faqMajorCategoryName;

    @Schema(description = "대분류 정렬 순서", example = "1")
    private Integer faqMajorCategoryOrder;

    public AdminSupportFaqMajorCategoryDto(AdminSupportFaqMajorCategoryEntity entity) {
        this.faqMajorCategoryId = entity.getFaqMajorCategoryId();
        this.faqMajorCategoryName = entity.getFaqMajorCategoryName();
        this.faqMajorCategoryOrder = entity.getFaqMajorCategoryOrder();
    }
}
