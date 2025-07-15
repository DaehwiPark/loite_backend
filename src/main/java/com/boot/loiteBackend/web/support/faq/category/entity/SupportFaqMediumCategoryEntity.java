package com.boot.loiteBackend.web.support.faq.category.entity;

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
public class SupportFaqMediumCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_MEDIUM_CATEGORY_ID")
    private Long faqMediumCategoryId;

    @Column(name = "FAQ_MEDIUM_CATEGORY_NAME", nullable = false, length = 100)
    private String faqMediumCategoryName;

    @Column(name = "FAQ_MEDIUM_CATEGORY_ORDER")
    private Integer faqMediumCategoryOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAQ_MAJOR_CATEGORY_ID", nullable = false)
    private SupportFaqMajorCategoryEntity faqMajorCategory;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
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
