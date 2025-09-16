package com.boot.loiteBackend.domain.manager.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_manager_notice_read",
        uniqueConstraints = @UniqueConstraint(name="uk_notice_manager", columnNames={"notice_id","manager_id"}))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AdminManagerNoticeRead {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="notice_id", nullable=false)
    private Long noticeId;

    @Column(name="manager_id", nullable=false)
    private Long managerId;

    @Column(nullable=false)
    private LocalDateTime readAt;

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.readAt == null) this.readAt = LocalDateTime.now();
    }
}