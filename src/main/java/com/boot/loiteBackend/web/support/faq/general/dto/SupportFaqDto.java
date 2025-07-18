package com.boot.loiteBackend.web.support.faq.general.dto;

import com.boot.loiteBackend.web.support.faq.general.entity.SupportFaqEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportFaqDto {
    private Long faqId;
    private String faqQuestion;
    private String faqAnswer;
    private String displayYn;
    private String deleteYn;

    public static SupportFaqDto from(SupportFaqEntity entity) {
        return SupportFaqDto.builder()
                .faqId(entity.getFaqId())
                .faqQuestion(entity.getFaqQuestion())
                .faqAnswer(entity.getFaqAnswer())
                .displayYn(entity.getDisplayYn())
                .deleteYn(entity.getDeleteYn())
                .build();
    }
}
