package com.boot.loiteMsBack.support.dto;

import com.boot.loiteMsBack.support.entity.SupportFaqEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportFaqDto {
    private Long faqId;
    private String faqTitle;
    private String faqContent;
    private String faqCategoryName;
    private String delYn;

    public SupportFaqDto(SupportFaqEntity entity) {
        this.faqId = entity.getFaqId();
        this.faqTitle = entity.getFaqTitle();
        this.faqContent = entity.getFaqContent();
        this.faqCategoryName = entity.getFaqCategory().getFaqCategoryName();
        this.delYn = entity.getDelYn();
    }

}
