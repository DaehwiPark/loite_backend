package com.boot.loiteBackend.admin.support.suggestion.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "경영진 제안 첨부파일 요약 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSupportSuggestionFileSummaryDto {

    @Schema(description = "제안 파일 고유 ID")
    private Long suggestionFileId;

    @Schema(description = "제안 ID (tb_support_suggestion.SUGGESTION_ID)")
    private Long suggestionId;

    @Schema(description = "저장된 파일명 (UUID.확장자 등)")
    private String suggestionFileName;

    @Schema(description = "웹 접근 경로 (/uploads/support/...)")
    private String suggestionFileUrl;

    @Schema(description = "파일 MIME 타입", example = "image/png")
    private String suggestionFileType;
}
