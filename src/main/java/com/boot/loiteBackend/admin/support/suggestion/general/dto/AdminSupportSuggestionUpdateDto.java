package com.boot.loiteBackend.admin.support.suggestion.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "제안 검토 상태 수정 DTO")
public class AdminSupportSuggestionUpdateDto {

    @Schema(description = "검토 상태 (예: 검토중, 검토완료)", example = "검토완료")
    private String suggestionReviewStatus;

    @Schema(description = "검토자 이름 또는 ID", example = "admin01")
    private String suggestionReviewer;

    @Schema(description = "검토 완료 일시", example = "2025-06-05T15:00:00")
    private LocalDateTime suggestionReviewedAt;
}
