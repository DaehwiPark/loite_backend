package com.boot.loiteBackend.admin.news.general.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminNewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NEWS_ID", columnDefinition = "BIGINT COMMENT '식별자'")
    private Long newsId;

    @Column(name = "NEWS_TITLE", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '제목'")
    private String newsTitle;

    @Column(name = "NEWS_CONTACT", columnDefinition = "VARCHAR(100) COMMENT '문의처 (전화번호 등)'")
    private String newsContact;

    @Column(name = "NEWS_THUMBNAIL_URL", columnDefinition = "VARCHAR(500) COMMENT '썸네일 이미지 또는 대표 이미지 URL'")
    private String newsThumbnailUrl;

    @Lob
    @Column(name = "NEWS_CONTENT", columnDefinition = "TEXT COMMENT '본문 내용 (HTML 또는 텍스트)'")
    private String newsContent;

    @Column(
            name = "CREATED_AT",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일'"
    )
    private LocalDateTime createdAt;

    @Column(
            name = "UPDATED_AT",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'"
    )
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