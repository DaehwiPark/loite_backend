package com.boot.loiteBackend.policy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_policy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POLICY_ID", columnDefinition = "BIGINT(20) COMMENT '정책 ID'")
    private Long policyId;

    @Column(name = "POLICY_TITLE", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '정책 제목'")
    private String policyTitle;

    @Column(name = "POLICY_CONTENT", nullable = false, columnDefinition = "TEXT COMMENT '정책 본문 (HTML 포함)'")
    private String policyContent;

    @Column(name = "DISPLAY_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y' COMMENT '노출 여부 (Y:노출/N:비노출)'")
    private String displayYn;

    @Column(name = "CREATED_AT", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
