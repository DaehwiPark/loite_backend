package com.boot.loiteBackend.admin.news.image.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_news_image")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminNewsImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NEWS_IMAGE_ID", columnDefinition = "BIGINT COMMENT '이미지 식별자'")
    private Long id;

    @Column(name = "NEWS_ID", nullable = false, columnDefinition = "BIGINT COMMENT '연결된 뉴스 ID'")
    private Long newsId;

    @Column(name = "NEWS_IMAGE_FILE_NAME", nullable = false, length = 255,
            columnDefinition = "VARCHAR(255) COMMENT '저장된 파일명 (UUID.확장자 등)'")
    private String fileName;

    @Column(name = "NEWS_IMAGE_FILE_URL", nullable = false, length = 500,
            columnDefinition = "VARCHAR(500) COMMENT '에디터에서 참조할 접근 URL (/uploads/news/...)'")
    private String fileUrl;

    @Column(name = "NEWS_IMAGE_FILE_PATH", nullable = false, length = 1000,
            columnDefinition = "VARCHAR(1000) COMMENT '서버 내 실제 파일 경로 (ex. /var/www/uploads/news/...)'")
    private String filePath;

    @Column(name = "NEWS_IMAGE_FILE_TYPE", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '파일 MIME 타입 (image/png 등)'")
    private String fileType;

    @Column(name = "NEWS_IMAGE_FILE_SIZE",
            columnDefinition = "INT DEFAULT NULL COMMENT '파일 크기 (Byte)'")
    private Integer fileSize;

    @Column(name = "CREATED_AT", updatable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '업로드 일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT",
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}