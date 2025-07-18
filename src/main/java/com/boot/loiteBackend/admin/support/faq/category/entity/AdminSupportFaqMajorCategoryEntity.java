package com.boot.loiteBackend.admin.support.faq.category.entity;

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
public class AdminSupportFaqMajorCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_MAJOR_CATEGORY_ID")
    private Long faqMajorCategoryId;

    @Column(name = "FAQ_MAJOR_CATEGORY_NAME", nullable = false, length = 100)
    private String faqMajorCategoryName;

    @Column(name = "FAQ_MAJOR_CATEGORY_ORDER")
    private Integer faqMajorCategoryOrder;

    @OneToMany(mappedBy = "faqMajorCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminSupportFaqMediumCategoryEntity> mediumCategories;

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
