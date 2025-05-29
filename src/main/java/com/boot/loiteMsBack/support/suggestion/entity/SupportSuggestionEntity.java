package com.boot.loiteMsBack.support.suggestion.entity;

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

    @Column(name = "SUGGESTION_SEND_EMAIL")
    private Boolean suggestionSendEmail;

    @Column(name = "SUGGESTION_SENT_AT")
    private LocalDateTime suggestionSentAt;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "DEL_YN", nullable = false)
    private String delYn = "N";
}
