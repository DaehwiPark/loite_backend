package com.boot.loiteMsBack.notice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")
    private Long noticeId;

    @Column(name = "NOTICE_TITLE", nullable = false, length = 200)
    private String noticeTitle;

    @Column(name = "NOTICE_CONTENT", columnDefinition = "TEXT", nullable = false)
    private String noticeContent;

    @Column(name = "NOTICE_VIEW_COUNT")
    private Integer noticeViewCount = 0;

    @Column(name = "DELETE_YN", length = 1)
    private String deleteYn = "N";

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
