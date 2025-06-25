package com.boot.loiteBackend.admin.support.suggestion.general.dto;

import com.boot.loiteBackend.admin.support.suggestion.file.dto.AdminSupportSuggestionFileSummaryDto;
import com.boot.loiteBackend.admin.user.dto.AdminUserSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "경영진 제안 DTO")
public class AdminSupportSuggestionDto {

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

    @Schema(description = "검토 상태 (검토중, 검토완료)", example = "검토중")
    private String suggestionReviewStatus;

    @Schema(description = "검토자 이름 또는 ID", example = "admin01")
    private String suggestionReviewer;

    @Schema(description = "검토 완료 일시", example = "2025-05-30T14:00:00")
    private LocalDateTime suggestionReviewedAt;

    @Schema(description = "제안 등록 일시", example = "2025-05-28T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "제안 수정 일시", example = "2025-05-28T10:10:00")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 여부 (N: 정상, Y: 삭제됨)", example = "N")
    private String deleteYn;

    @Schema(description = "첨부파일 목록")
    private List<AdminSupportSuggestionFileSummaryDto> filesSummaryDto;

    @Schema(description = "제안자 정보 (이름, 이메일, 권한 등)")
    private AdminUserSummaryDto adminUserSummaryDto;

}
