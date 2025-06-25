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
    @Column(name = "NEWS_IMAGE_ID")
    private Long id;

    @Column(name = "NEWS_ID", nullable = false)
    private Long newsId;

    @Column(name = "NEWS_IMAGE_FILE_NAME", nullable = false, length = 255)
    private String fileName;

    @Column(name = "NEWS_IMAGE_FILE_URL", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "NEWS_IMAGE_FILE_PATH", nullable = false, length = 1000)
    private String filePath;

    @Column(name = "NEWS_IMAGE_FILE_TYPE", length = 100)
    private String fileType;

    @Column(name = "NEWS_IMAGE_FILE_SIZE")
    private Integer fileSize;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
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
