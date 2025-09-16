// domain/notice/ManagerNotice.java
package com.boot.loiteBackend.domain.manager.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_manager_notice")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AdminManagerNoticeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Lob
    @Column(name="content_md", nullable=false, columnDefinition = "MEDIUMTEXT")
    private String contentMd;

    @Column(nullable=false)
    private Integer importance; // 0/1

    @Column(nullable=false)
    private Boolean pinned;     // true/false

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=16)
    private NoticeStatus status; // DRAFT/PUBLISHED/ARCHIVED

    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;

    @Column(nullable=false)
    private Long createdByAdmin;

    private Long updatedByAdmin;

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = NoticeStatus.PUBLISHED;
        if (this.importance == null) this.importance = 0;
        if (this.pinned == null) this.pinned = false;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isVisibleNow() {
        if (deletedAt != null) return false;
        if (status != NoticeStatus.PUBLISHED) return false;
        return (expiresAt == null) || expiresAt.isAfter(LocalDateTime.now());
    }

    public enum NoticeStatus { DRAFT, PUBLISHED, ARCHIVED }
}
