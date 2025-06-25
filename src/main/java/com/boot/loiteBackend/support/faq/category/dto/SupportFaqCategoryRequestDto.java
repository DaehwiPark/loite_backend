package com.boot.loiteBackend.support.faq.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "FAQ 카테고리 생성 및 수정 요청 DTO")
@Data
public class SupportFaqCategoryRequestDto {

    @Schema(description = "카테고리 이름", example = "서비스 이용", required = true)
    private String faqCategoryName;

    @Schema(description = "카테고리 정렬 순서", example = "2", required = true)
    private Integer faqCategoryOrder;
}
