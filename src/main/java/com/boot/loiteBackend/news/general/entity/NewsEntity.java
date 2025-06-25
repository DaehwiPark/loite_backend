package com.boot.loiteBackend.news.general.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "새소식 엔티티")
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "뉴스 식별자", example = "1")
    private Long newsId;

    @Column(nullable = false)
    @Schema(description = "뉴스 제목", example = "서비스 점검 안내")
    private String newsTitle;

    @Schema(description = "문의처 (전화번호 등)", example = "02-1234-5678")
    private String newsContact;

    @Schema(description = "썸네일 이미지 URL", example = "https://cdn.loite.com/thumb.jpg")
    private String newsThumbnailUrl;

    @Lob
    @Schema(description = "본문 내용 (HTML 가능)", example = "<p>점검 안내입니다.</p>")
    private String newsContent;

    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-05-30T14:35:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-06-01T09:20:00")
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
