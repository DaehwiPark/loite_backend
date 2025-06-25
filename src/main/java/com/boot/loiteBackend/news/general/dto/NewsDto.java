package com.boot.loiteBackend.news.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "새소식 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDto {

    @Schema(description = "뉴스 식별자", example = "1")
    private Long newsId;

    @Schema(description = "뉴스 제목", example = "서비스 점검 안내")
    private String newsTitle;

    @Schema(description = "문의처 (전화번호 등)", example = "02-1234-5678")
    private String newsContact;

    @Schema(description = "썸네일 이미지 URL", example = "https://cdn.loite.com/thumb.jpg")
    private String newsThumbnailUrl;

    @Schema(description = "본문 내용 (HTML 가능)", example = "<p>점검 안내입니다.</p>")
    private String newsContent;

    @Schema(description = "생성일시", example = "2024-05-30T14:35:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-06-01T09:20:00")
    private LocalDateTime updatedAt;
}
