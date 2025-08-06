package com.boot.loiteBackend.web.support.faq.general.dto;

import com.boot.loiteBackend.web.support.faq.general.entity.SupportFaqEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "FAQ 데이터 DTO")
public class SupportFaqDto {

    @Schema(description = "FAQ 고유 ID", example = "1001")
    private Long faqId;

    @Schema(description = "질문 제목", example = "비밀번호를 변경하려면 어떻게 하나요?")
    private String faqQuestion;

    @Schema(description = "답변 내용", example = "마이페이지 > 계정 설정에서 비밀번호를 변경할 수 있습니다.")
    private String faqAnswer;

    @Schema(description = "노출 여부 (Y: 노출, N: 비노출)", example = "Y")
    private String displayYn;

    @Schema(description = "삭제 여부 (Y: 삭제됨, N: 유지)", example = "N")
    private String deleteYn;

    @Schema(description = "중분류 카테고리 이름", example = "로그인/비밀번호")
    private String faqMediumCategoryName;

    public static SupportFaqDto from(SupportFaqEntity entity) {
        return SupportFaqDto.builder()
                .faqId(entity.getFaqId())
                .faqQuestion(entity.getFaqQuestion())
                .faqAnswer(entity.getFaqAnswer())
                .displayYn(entity.getDisplayYn())
                .deleteYn(entity.getDeleteYn())
                .faqMediumCategoryName(entity.getFaqCategory().getFaqMediumCategoryName())
                .build();
    }
}