package com.boot.loiteBackend.domain.support.faq.general.entity;

import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_faq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SupportFaqEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_ID", columnDefinition = "BIGINT(20) COMMENT '기본 키'")
    private Long faqId;

    @Column(name = "FAQ_QUESTION", nullable = false, length = 255,
            columnDefinition = "VARCHAR(255) NOT NULL COMMENT '질문 제목'")
    private String faqQuestion;

    @Column(name = "FAQ_ANSWER", nullable = false, length = 255,
            columnDefinition = "VARCHAR(255) NOT NULL COMMENT '질문에 대한 답변 내용'")
    private String faqAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAQ_MEDIUM_CATEGORY_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_FAQ_CATEGORY"))
    private SupportFaqMediumCategoryEntity faqCategory;

    @Column(name = "DISPLAY_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '노출 여부'")
    private String displayYn;

    @Column(name = "DELETE_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '삭제 여부'")
    private String deleteYn;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'")
    private LocalDateTime updatedAt;
}