package com.boot.loiteBackend.support.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "공지사항 등록/수정 요청 DTO")
public class SupportNoticeRequestDto {

    @Schema(description = "공지 제목", example = "서비스 점검 안내", required = true)
    private String noticeTitle;

    @Schema(description = "공지 내용", example = "서비스 점검이 5월 30일 01:00~02:00 동안 진행됩니다.", required = true)
    private String noticeContent;

    @Schema(description = "상단 고정 여부", example = "N", required = true)
    private String pinnedYn;

    @Schema(description = "노출 여부", example = "Y", required = true)
    private String displayYn;
}
