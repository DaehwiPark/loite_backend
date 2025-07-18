package com.boot.loiteBackend.admin.support.suggestion.general.entity;

import com.boot.loiteBackend.admin.user.Entity.AdminUserEntity;
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
public class AdminSupportSuggestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUGGESTION_ID")
    private Long suggestionId;

    @Column(name = "SUGGESTION_USER_ID")
    private Long suggestionUserId;

    @Column(name = "SUGGESTION_TITLE", nullable = false)
    private String suggestionTitle;

    @Column(name = "SUGGESTION_CONTENT", nullable = false)
    private String suggestionContent;

    @Column(name = "SUGGESTION_EMAIL")
    private String suggestionEmail;

    @Column(name = "SUGGESTION_REVIEW_STATUS")
    private String suggestionReviewStatus;

    @Column(name = "SUGGESTION_REVIEWER")
    private String suggestionReviewer;

    @Column(name = "SUGGESTION_REVIEWED_AT")
    private LocalDateTime suggestionReviewedAt;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "DELETE_YN", nullable = false)
    private String deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUGGESTION_USER_ID", insertable = false, updatable = false)
    private AdminUserEntity user;
}
