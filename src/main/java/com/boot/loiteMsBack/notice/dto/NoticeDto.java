package com.boot.loiteMsBack.notice.dto;

import com.boot.loiteMsBack.notice.entity.NoticeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공지사항 DTO")
public class NoticeDto {

    @Schema(description = "공지 ID", example = "1")
    private Long noticeId;

    @Schema(description = "공지 제목", example = "서비스 점검 안내")
    private String noticeTitle;

    @Schema(description = "공지 내용", example = "금일 23시부터 서버 점검이 예정되어 있습니다.")
    private String noticeContent;

    @Schema(description = "조회수", example = "120")
    private Integer viewCount;

    @Schema(description = "등록일", example = "2025-05-29T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2025-05-29T10:00:00")
    private LocalDateTime updatedAt;

    public NoticeDto(NoticeEntity entity) {
        this.noticeId = entity.getNoticeId();
        this.noticeTitle = entity.getNoticeTitle();
        this.noticeContent = entity.getNoticeContent();
        this.viewCount = entity.getViewCount();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
