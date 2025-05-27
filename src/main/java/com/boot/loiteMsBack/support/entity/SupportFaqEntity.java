package com.boot.loiteMsBack.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_faq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportFaqEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_ID")
    private Long faqId;

    @Column(name = "FAQ_TITLE", nullable = false, length = 255)
    private String faqTitle;

    @Column(name = "FAQ_CONTENT", nullable = false, length = 255)
    private String faqContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAQ_CATEGORY_ID")
    private SupportFaqCategoryEntity faqCategory;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "DEL_YN")
    private String delYn;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
