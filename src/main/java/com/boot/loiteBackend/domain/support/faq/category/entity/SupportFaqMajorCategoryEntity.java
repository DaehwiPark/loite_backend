package com.boot.loiteBackend.domain.support.faq.category.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_support_faq_major_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportFaqMajorCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_MAJOR_CATEGORY_ID", columnDefinition = "BIGINT COMMENT 'FAQ 대분류 카테고리 고유 ID'")
    private Long faqMajorCategoryId;

    @Column(name = "FAQ_MAJOR_CATEGORY_NAME", nullable = false, length = 100,
            columnDefinition = "VARCHAR(100) COMMENT 'FAQ 대분류 카테고리 이름'")
    private String faqMajorCategoryName;

    @Column(name = "FAQ_MAJOR_CATEGORY_ORDER", columnDefinition = "INT COMMENT 'FAQ 대분류 카테고리 정렬 순서'")
    private Integer faqMajorCategoryOrder;

    @OneToMany(mappedBy = "faqMajorCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportFaqMediumCategoryEntity> mediumCategories;

    @Column(name = "CREATED_AT", nullable = false, updatable = false,
            columnDefinition = "DATETIME COMMENT '등록일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "DATETIME COMMENT '수정일시'")
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