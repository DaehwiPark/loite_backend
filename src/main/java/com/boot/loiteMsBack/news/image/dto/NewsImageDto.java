package com.boot.loiteMsBack.news.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "뉴스 이미지 DTO")
public class NewsImageDto {

    @Schema(description = "이미지 식별자")
    private Long id;

    @Schema(description = "연결된 뉴스 ID")
    private Long newsId;

    @Schema(description = "저장된 파일명 (UUID.확장자)")
    private String fileName;

    @Schema(description = "접근 가능한 URL (/uploads/news/...)")
    private String fileUrl;

    @Schema(description = "서버 내 실제 파일 경로")
    private String filePath;

    @Schema(description = "파일 MIME 타입")
    private String fileType;

    @Schema(description = "파일 크기 (Byte)")
    private Integer fileSize;

    @Schema(description = "업로드 일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;
}
