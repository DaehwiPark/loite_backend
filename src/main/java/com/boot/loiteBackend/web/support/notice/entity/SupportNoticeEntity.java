package com.boot.loiteBackend.web.support.notice.entity;

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
public class SupportNoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID", columnDefinition = "BIGINT(20) COMMENT '공지사항 고유 ID'")
    private Long noticeId;

    @Column(name = "NOTICE_TITLE", nullable = false, length = 200, columnDefinition = "VARCHAR(200) COMMENT '공지 제목'")
    private String noticeTitle;

    @Column(name = "NOTICE_CONTENT", nullable = false, columnDefinition = "TEXT COMMENT '공지 내용'")
    private String noticeContent;

    @Column(name = "NOTICE_VIEW_COUNT", columnDefinition = "INT(11) DEFAULT 0 COMMENT '조회수'")
    private Integer noticeViewCount;

    @Column(name = "PINNED_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N' COMMENT '상단 고정 여부(Y/N)'")
    private String pinnedYn;

    @Column(name = "DISPLAY_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y' COMMENT '노출 여부(Y/N)'")
    private String displayYn;

    @Column(name = "CREATED_AT", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '등록일'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'")
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

    public void insertViewCount() {
        if (this.noticeViewCount == null) {
            this.noticeViewCount = 1;
        } else {
            this.noticeViewCount += 1;
        }
    }
}
