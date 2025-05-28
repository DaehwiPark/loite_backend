package com.boot.loiteMsBack.support.faq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "FAQ 카테고리 생성 및 수정 요청 DTO")
@Getter
@Setter
public class SupportFaqCategoryRequestDto {

    @Schema(description = "카테고리 이름", example = "서비스 이용", required = true)
    private String faqCategoryName;

    @Schema(description = "카테고리 정렬 순서", example = "2", required = true)
    private Integer faqCategoryOrder;
}
