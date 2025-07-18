package com.boot.loiteBackend.admin.support.faq.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "FAQ 대분류 카테고리 생성 및 수정 요청 DTO")
@Data
public class AdminSupportFaqMajorCategoryRequestDto {

    @Schema(description = "대분류 카테고리 이름", example = "회원 서비스", required = true)
    private String faqMajorCategoryName;

    @Schema(description = "대분류 카테고리 정렬 순서", example = "1", required = true)
    private Integer faqMajorCategoryOrder;
}
