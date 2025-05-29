package com.boot.loiteMsBack.support.faq.question.entity;

import com.boot.loiteMsBack.support.faq.category.entity.SupportFaqCategoryEntity;
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

    @Column(name = "FAQ_QUESTION", nullable = false, length = 255)
    private String faqQuestion;

    @Column(name = "FAQ_ANSWER", nullable = false, length = 255)
    private String faqAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAQ_CATEGORY_ID")
    private SupportFaqCategoryEntity faqCategory;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

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