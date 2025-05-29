package com.boot.loiteMsBack.support.suggestion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "경영진 제안 DTO")
public class SupportSuggestionDto {

    @Schema(description = "제안 ID (기본 키)", example = "1")
    private Long suggestionId;

    @Schema(description = "제안 작성자 사용자 ID", example = "1001")
    private Long suggestionUserId;

    @Schema(description = "제안 제목", example = "업무 효율화 아이디어 제안")
    private String suggestionTitle;

    @Schema(description = "제안 상세 내용", example = "AI 기반 자동 보고서 생성 기능을 도입하면...")
    private String suggestionContent;

    @Schema(description = "제안자 이메일", example = "user@example.com")
    private String suggestionEmail;

    @Schema(description = "메일 발송 여부 (0: 미발송, 1: 발송됨)", example = "true")
    private Boolean suggestionSendEmail;

    @Schema(description = "메일 발송 일시", example = "2025-05-28T10:30:00")
    private LocalDateTime suggestionSentAt;

    @Schema(description = "제안 등록 일시", example = "2025-05-28T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "제안 수정 일시", example = "2025-05-28T10:10:00")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 여부 (N: 정상, Y: 삭제됨)", example = "N")
    private String delYn;
}
