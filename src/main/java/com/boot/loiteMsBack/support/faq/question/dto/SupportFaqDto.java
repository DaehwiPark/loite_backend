package com.boot.loiteMsBack.support.faq.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "FAQ 응답 DTO")
@Builder
@Data
public class SupportFaqDto {

    @Schema(description = "FAQ ID", example = "1")
    private Long faqId;

    @Schema(description = "질문", example = "비밀번호를 변경하려면 어떻게 하나요?")
    private String faqQuestion;

    @Schema(description = "답변", example = "마이페이지 > 보안 설정에서 변경 가능합니다.")
    private String faqAnswer;

    @Schema(description = "FAQ 카테고리 이름", example = "계정 관련")
    private String faqCategoryName;

    @Schema(description = "등록일시", example = "2025-05-29T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-05-29T12:00:00")
    private LocalDateTime updatedAt;
}
