package com.boot.loiteBackend.web.support.faq.category.dto;

import com.boot.loiteBackend.web.support.faq.category.entity.SupportFaqMajorCategoryEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupportFaqMajorCategoryDto {

    private Long faqMajorCategoryId;
    private String faqMajorCategoryName;
    private Integer faqMajorCategoryOrder;

    public static SupportFaqMajorCategoryDto from(SupportFaqMajorCategoryEntity entity) {
        return SupportFaqMajorCategoryDto.builder()
                .faqMajorCategoryId(entity.getFaqMajorCategoryId())
                .faqMajorCategoryName(entity.getFaqMajorCategoryName())
                .faqMajorCategoryOrder(entity.getFaqMajorCategoryOrder())
                .build();
    }
}
