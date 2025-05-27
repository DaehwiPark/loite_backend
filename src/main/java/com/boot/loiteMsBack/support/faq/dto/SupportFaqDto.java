package com.boot.loiteMsBack.support.faq.dto;

import com.boot.loiteMsBack.support.faq.entity.SupportFaqEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "FAQ 응답 DTO")
@Getter
@Setter
public class SupportFaqDto {

    @Schema(description = "FAQ ID", example = "1")
    private Long faqId;

    @Schema(description = "질문", example = "비밀번호를 변경하려면 어떻게 하나요?")
    private String faqQuestion;

    @Schema(description = "답변", example = "마이페이지 > 보안 설정에서 변경 가능합니다.")
    private String faqAnswer;

    @Schema(description = "FAQ 카테고리 이름", example = "계정 관련")
    private String faqCategoryName;

    public SupportFaqDto(SupportFaqEntity entity) {
        this.faqId = entity.getFaqId();
        this.faqQuestion = entity.getFaqQuestion();
        this.faqAnswer = entity.getFaqAnswer();
        this.faqCategoryName = entity.getFaqCategory().getFaqCategoryName();
    }
}
