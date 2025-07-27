package com.boot.loiteBackend.admin.news.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "뉴스 이미지 DTO")
public class AdminNewsImageDto {

    @Schema(description = "이미지 식별자", example = "10")
    private Long id;

    @Schema(description = "연결된 뉴스 ID", example = "1")
    private Long newsId;

    @Schema(description = "저장된 파일명 (UUID.확장자)", example = "b42f9a78-5e7f-43dd-98e1-aceb58a16f88.jpg")
    private String fileName;

    @Schema(description = "접근 가능한 URL (/uploads/news/...)", example = "https://cdn.loite.com/uploads/news/b42f9a78-5e7f.jpg")
    private String fileUrl;

    @Schema(description = "서버 내 실제 파일 경로", example = "/var/www/uploads/news/b42f9a78-5e7f.jpg")
    private String filePath;

    @Schema(description = "파일 MIME 타입", example = "image/jpeg")
    private String fileType;

    @Schema(description = "파일 크기 (Byte)", example = "245638")
    private Integer fileSize;

    @Schema(description = "업로드 일시", example = "2025-07-26T14:35:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-07-26T14:35:00")
    private LocalDateTime updatedAt;
}