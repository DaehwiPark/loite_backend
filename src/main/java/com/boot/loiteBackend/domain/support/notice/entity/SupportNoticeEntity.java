package com.boot.loiteBackend.domain.support.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SupportNoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID", columnDefinition = "BIGINT(20) COMMENT '공지사항 고유 ID'")
    private Long noticeId;

    @Column(name = "NOTICE_TITLE", nullable = false, length = 200,
            columnDefinition = "VARCHAR(200) NOT NULL COMMENT '공지 제목'")
    private String noticeTitle;

    @Column(name = "NOTICE_CONTENT", nullable = false,
            columnDefinition = "TEXT NOT NULL COMMENT '공지 내용'")
    private String noticeContent;

    @Column(name = "NOTICE_VIEW_COUNT",
            columnDefinition = "INT(11) DEFAULT 0 COMMENT '조회수'")
    private Integer noticeViewCount;

    @Column(name = "PINNED_YN", nullable = false,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '상단 고정 여부(Y/N)'")
    private String pinnedYn;

    @Column(name = "DISPLAY_YN", nullable = false,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '노출 여부(Y/N)'")
    private String displayYn;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '등록일'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'")
    private LocalDateTime updatedAt;

    public void insertViewCount() {
        if (this.noticeViewCount == null) {
            this.noticeViewCount = 1;
        } else {
            this.noticeViewCount += 1;
        }
    }
}