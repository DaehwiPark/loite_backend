package com.boot.loiteBackend.domain.support.suggestion.general.entity;

import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_suggestion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportSuggestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUGGESTION_ID", columnDefinition = "BIGINT COMMENT '기본 키 (제안 고유 ID)'")
    private Long suggestionId;

    @Column(name = "SUGGESTION_USER_ID", columnDefinition = "BIGINT DEFAULT NULL COMMENT '제안 작성자 사용자 ID (tb_user.USER_ID 참조)'")
    private Long suggestionUserId;

    @Column(name = "SUGGESTION_TITLE", nullable = false, length = 255,
            columnDefinition = "VARCHAR(255) COMMENT '제안 제목'")
    private String suggestionTitle;

    @Column(name = "SUGGESTION_CONTENT", nullable = false, length = 255,
            columnDefinition = "VARCHAR(255) COMMENT '제안 상세 내용'")
    private String suggestionContent;

    @Column(name = "SUGGESTION_EMAIL", length = 255,
            columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '제안자 이메일'")
    private String suggestionEmail;

    @Column(name = "SUGGESTION_REVIEW_STATUS", length = 20, nullable = false,
            columnDefinition = "VARCHAR(20) NOT NULL DEFAULT '검토대기' COMMENT '검토 상태 (검토대기, 검토중, 검토완료)'")
    private String suggestionReviewStatus;

    @Column(name = "SUGGESTION_REVIEWER", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '검토자 이름 또는 ID'")
    private String suggestionReviewer;

    @Column(name = "SUGGESTION_REVIEWED_AT",
            columnDefinition = "DATETIME(6) DEFAULT NULL COMMENT '검토 완료 일시'")
    private LocalDateTime suggestionReviewedAt;

    @Column(name = "CREATED_AT",
            columnDefinition = "DATETIME(6) DEFAULT NULL COMMENT '제안 등록 일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT",
            columnDefinition = "DATETIME(6) DEFAULT NULL COMMENT '제안 수정 일시'")
    private LocalDateTime updatedAt;

    @Column(name = "DELETE_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '삭제여부(N/Y)'")
    private String deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUGGESTION_USER_ID", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_SUGGESTION_USER_ID"))
    private UserEntity user;
}