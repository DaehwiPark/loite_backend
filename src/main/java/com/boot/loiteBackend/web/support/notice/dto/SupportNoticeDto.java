package com.boot.loiteBackend.web.support.notice.dto;

import com.boot.loiteBackend.admin.support.notice.entity.AdminSupportNoticeEntity;
import com.boot.loiteBackend.web.support.notice.entity.SupportNoticeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공지사항 DTO")
public class SupportNoticeDto {

    @Schema(description = "공지 ID", example = "1")
    private Long noticeId;

    @Schema(description = "공지 제목", example = "서비스 점검 안내")
    private String noticeTitle;

    @Schema(description = "공지 내용", example = "금일 23시부터 서버 점검이 예정되어 있습니다.")
    private String noticeContent;

    @Schema(description = "조회수", example = "120")
    private Integer noticeViewCount;

    @Schema(description = "노출 여부 (Y/N)", example = "Y")
    private String displayYn;

    @Schema(description = "상단 고정 여부 (Y/N)", example = "N")
    private String pinnedYn;

    @Schema(description = "등록일시", example = "2025-05-29T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-05-29T10:00:00")
    private LocalDateTime updatedAt;

    public SupportNoticeDto(SupportNoticeEntity entity) {
        this.noticeId = entity.getNoticeId();
        this.noticeTitle = entity.getNoticeTitle();
        this.noticeContent = entity.getNoticeContent();
        this.noticeViewCount = entity.getNoticeViewCount();
        this.displayYn = entity.getDisplayYn();
        this.pinnedYn = entity.getPinnedYn();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
