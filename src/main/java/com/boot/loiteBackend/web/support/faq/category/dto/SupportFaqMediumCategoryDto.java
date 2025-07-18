package com.boot.loiteBackend.web.support.faq.category.dto;

import com.boot.loiteBackend.web.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupportFaqMediumCategoryDto {

    private Long faqMediumCategoryId;
    private String faqMediumCategoryName;
    private Integer faqMediumCategoryOrder;
    private Long faqMajorCategoryId;

    public static SupportFaqMediumCategoryDto from(SupportFaqMediumCategoryEntity entity) {
        return SupportFaqMediumCategoryDto.builder()
                .faqMediumCategoryId(entity.getFaqMediumCategoryId())
                .faqMediumCategoryName(entity.getFaqMediumCategoryName())
                .faqMediumCategoryOrder(entity.getFaqMediumCategoryOrder())
                .faqMajorCategoryId(entity.getFaqMajorCategory().getFaqMajorCategoryId())
                .build();
    }
}
