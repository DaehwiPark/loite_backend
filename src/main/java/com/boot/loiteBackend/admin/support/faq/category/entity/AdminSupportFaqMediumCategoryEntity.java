package com.boot.loiteBackend.admin.support.faq.category.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_faq_medium_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSupportFaqMediumCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_MEDIUM_CATEGORY_ID", columnDefinition = "BIGINT COMMENT '중분류 고유 ID'")
    private Long faqMediumCategoryId;

    @Column(name = "FAQ_MEDIUM_CATEGORY_NAME", nullable = false, length = 100, columnDefinition = "VARCHAR(100) COMMENT '중분류 이름'")
    private String faqMediumCategoryName;

    @Column(name = "FAQ_MEDIUM_CATEGORY_ORDER", columnDefinition = "INT DEFAULT 0 COMMENT '표시 순서'")
    private Integer faqMediumCategoryOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAQ_MAJOR_CATEGORY_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_MEDIUM_TO_MAJOR"))
    private AdminSupportFaqMajorCategoryEntity faqMajorCategory;

    @Column(name = "FAQ_IMAGE_NAME", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '업로드된 실제 파일명'")
    private String faqImageName;

    @Column(name = "FAQ_IMAGE_URL", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '카테고리 이미지 URL'")
    private String faqImageUrl;

    @Column(name = "FAQ_IMAGE_PATH", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '실제 서버 내 파일 경로'")
    private String faqImagePath;

    @Column(name = "CREATED_AT", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}