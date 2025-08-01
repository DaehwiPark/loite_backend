package com.boot.loiteBackend.admin.support.suggestion.file.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_suggestion_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSupportSuggestionFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUGGESTION_FILE_ID",
            columnDefinition = "BIGINT COMMENT '제안 파일 식별자'")
    private Long suggestionFileId;

    @Column(name = "SUGGESTION_ID", nullable = false,
            columnDefinition = "BIGINT COMMENT '연결된 제안 ID'")
    private Long suggestionId;

    @Column(name = "SUGGESTION_FILE_NAME", nullable = false, length = 255,
            columnDefinition = "VARCHAR(255) COMMENT '저장된 파일명 (UUID.확장자 등)'")
    private String suggestionFileName;

    @Column(name = "SUGGESTION_FILE_URL", nullable = false, length = 500,
            columnDefinition = "VARCHAR(500) COMMENT '웹 접근 URL (/uploads/support/...)'")
    private String suggestionFileUrl;

    @Column(name = "SUGGESTION_FILE_PATH", nullable = false, length = 1000,
            columnDefinition = "VARCHAR(1000) COMMENT '서버 내 실제 저장 경로 (예: /var/www/uploads/support/...)'")
    private String suggestionFilePath;

    @Column(name = "SUGGESTION_FILE_TYPE", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '파일 MIME 타입 (image/png 등)'")
    private String suggestionFileType;

    @Column(name = "SUGGESTION_FILE_SIZE",
            columnDefinition = "INT DEFAULT NULL COMMENT '파일 크기 (Byte)'")
    private Integer suggestionFileSize;

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