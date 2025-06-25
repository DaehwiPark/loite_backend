package com.boot.loiteBackend.support.faq.general.entity;

import com.boot.loiteBackend.support.faq.category.entity.SupportFaqCategoryEntity;
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
    @Column(name = "FAQ_ID", columnDefinition = "BIGINT(20) COMMENT '기본 키'")
    private Long faqId;

    @Column(name = "FAQ_QUESTION", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '질문 제목'")
    private String faqQuestion;

    @Column(name = "FAQ_ANSWER", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '질문에 대한 답변 내용'")
    private String faqAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAQ_CATEGORY_ID",nullable = false,foreignKey = @ForeignKey(name = "FK_FAQ_CATEGORY"))
    private SupportFaqCategoryEntity faqCategory;

    @Column(name = "DISPLAY_YN", nullable = false, length = 1, columnDefinition = "CHAR(1) DEFAULT 'Y' COMMENT '노출 여부'")
    private String displayYn;

    @Column(name = "DELETE_YN", nullable = false, length = 1, columnDefinition = "CHAR(1) DEFAULT 'N' COMMENT '삭제 여부'")
    private String deleteYn;

    @Column(name = "CREATED_AT", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'")
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
