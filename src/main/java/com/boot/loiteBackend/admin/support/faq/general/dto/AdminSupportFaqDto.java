package com.boot.loiteBackend.admin.support.faq.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "FAQ 응답 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSupportFaqDto {

    @Schema(description = "FAQ ID", example = "1")
    private Long faqId;

    @Schema(description = "질문", example = "비밀번호를 변경하려면 어떻게 하나요?")
    private String faqQuestion;

    @Schema(description = "답변", example = "마이페이지 > 보안 설정에서 변경 가능합니다.")
    private String faqAnswer;

    @Schema(description = "중분류 카테고리 ID", example = "101")
    private Long faqMediumCategoryId;

    @Schema(description = "중분류 카테고리 이름", example = "계정 관리")
    private String faqMediumCategoryName;

    @Schema(description = "대분류 카테고리 ID", example = "10")
    private Long faqMajorCategoryId;

    @Schema(description = "대분류 카테고리 이름", example = "회원 서비스")
    private String faqMajorCategoryName;

    @Schema(description = "노출 여부", example = "Y")
    private String displayYn;

    @Schema(description = "삭제 여부", example = "N")
    private String deleteYn;

    @Schema(description = "등록일시", example = "2025-05-29T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-05-29T12:00:00")
    private LocalDateTime updatedAt;
}
