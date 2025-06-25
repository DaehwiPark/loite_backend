package com.boot.loiteBackend.terms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_terms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TERMS_ID", columnDefinition = "BIGINT")
    private Long termsId;

    @Column(name = "TERMS_TITLE", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '약관 제목'")
    private String termsTitle;

    @Column(name = "TERMS_CONTENT", nullable = false, columnDefinition = "TEXT COMMENT '약관 본문 (HTML 포함)'")
    private String termsContent;

    @Column(name = "DISPLAY_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y' COMMENT '노출 여부 (Y:노출/N:비노출)'")
    private String displayYn;

    @Column(name = "CREATED_AT", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
