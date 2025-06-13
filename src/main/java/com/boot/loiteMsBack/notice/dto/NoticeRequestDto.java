package com.boot.loiteMsBack.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "공지사항 등록/수정 요청 DTO")
public class NoticeRequestDto {

    @Schema(description = "공지 제목", example = "서비스 점검 안내", required = true)
    private String noticeTitle;

    @Schema(description = "공지 내용", example = "서비스 점검이 5월 30일 01:00~02:00 동안 진행됩니다.", required = true)
    private String noticeContent;
}
