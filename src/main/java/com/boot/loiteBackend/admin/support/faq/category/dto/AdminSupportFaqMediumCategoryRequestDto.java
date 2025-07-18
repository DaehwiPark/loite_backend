package com.boot.loiteBackend.admin.support.faq.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "FAQ 중분류 카테고리 생성 및 수정 요청 DTO")
@Data
public class AdminSupportFaqMediumCategoryRequestDto {

    @Schema(description = "중분류 카테고리 이름", example = "배송 관련", required = true)
    private String faqMediumCategoryName;

    @Schema(description = "중분류 카테고리 정렬 순서", example = "2", required = true)
    private Integer faqMediumCategoryOrder;

    @Schema(description = "대분류 카테고리 ID", example = "1", required = true)
    private Long faqMajorCategoryId;
}
