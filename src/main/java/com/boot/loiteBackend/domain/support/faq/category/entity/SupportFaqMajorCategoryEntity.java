package com.boot.loiteBackend.domain.support.faq.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_support_faq_major_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SupportFaqMajorCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_MAJOR_CATEGORY_ID", columnDefinition = "BIGINT(20) COMMENT 'FAQ 대분류 카테고리 고유 ID'")
    private Long faqMajorCategoryId;

    @Column(name = "FAQ_MAJOR_CATEGORY_NAME", nullable = false, length = 100,
            columnDefinition = "VARCHAR(100) NOT NULL COMMENT 'FAQ 대분류 카테고리 이름'")
    private String faqMajorCategoryName;

    @Column(name = "FAQ_MAJOR_CATEGORY_ORDER", columnDefinition = "INT(11) DEFAULT 0 COMMENT 'FAQ 대분류 카테고리 정렬 순서'")
    private Integer faqMajorCategoryOrder;

    @OneToMany(mappedBy = "faqMajorCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportFaqMediumCategoryEntity> mediumCategories;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;
}