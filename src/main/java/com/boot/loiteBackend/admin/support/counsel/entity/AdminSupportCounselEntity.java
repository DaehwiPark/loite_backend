package com.boot.loiteBackend.admin.support.counsel.entity;

import com.boot.loiteBackend.admin.support.counsel.enums.AdminCounselStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_counsel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSupportCounselEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNSEL_ID", columnDefinition = "BIGINT COMMENT '기본 키 (1:1 문의 ID)'")
    private Long counselId;

    @Column(name = "COUNSEL_USER_ID", nullable = false, columnDefinition = "BIGINT COMMENT '문의한 사용자 ID (tb_user.USER_ID 참조)'")
    private Long counselUserId;

    @Column(name = "COUNSEL_TITLE", nullable = false, columnDefinition = "VARCHAR(100) COMMENT '문의 제목 (예: 배송 지연 문의)'")
    private String counselTitle;

    @Column(name = "COUNSEL_CONTENT", nullable = false, columnDefinition = "VARCHAR(500) COMMENT '문의 상세 내용'")
    private String counselContent;

    @Column(name = "COUNSEL_EMAIL", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '답변을 받을 이메일 주소'")
    private String counselEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "COUNSEL_STATUS", nullable = false, columnDefinition = "VARCHAR(30) DEFAULT 'WAITING' COMMENT '문의 상태 (예: 대기/WAITING, 완료/COMPLETE)'")
    private AdminCounselStatus adminCounselStatus;

    @Column(name = "COUNSEL_REPLY_CONTENT", columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT '관리자의 답변 내용'")
    private String counselReplyContent;

    @Column(name = "COUNSEL_REPLIED_AT", columnDefinition = "DATETIME(6) DEFAULT NULL COMMENT '답변이 작성된 일시'")
    private LocalDateTime counselRepliedAt;

    @Column(name = "COUNSEL_REPLIED_BY", columnDefinition = "BIGINT DEFAULT NULL COMMENT '답변한 관리자 ID (tb_user.USER_ID 참조)'")
    private Long counselRepliedBy;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '문의 작성일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '문의 수정일시'")
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N' COMMENT '삭제 여부 (Y:삭제, N:삭제 아님)'")
    private String deleteYn = "N";

    @Builder.Default
    @Column(name = "COUNSEL_PRIVATE_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N' COMMENT '비밀글 여부 (Y: 비밀글, N: 일반글)'")
    private String counselPrivateYn = "N";

    @Column(name = "COUNSEL_PRIVATE_PASSWORD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '비밀글 비밀번호 (암호화된 값)'")
    private String counselPrivatePassword;

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