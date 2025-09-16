package com.boot.loiteBackend.domain.manager.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_manager_notice_attachment")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AdminManagerNoticeAttachment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="notice_id", nullable=false)
    private Long noticeId;

    @Column(name="file_url", nullable=false, length=500)
    private String fileUrl;

    @Column(name="file_path", nullable=false, length=1000)
    private String filePath;

    @Column(name="original_name", nullable=false, length=255)
    private String originalName;

    @Column(name="mime_type", length=100)
    private String mimeType;

    @Column(name="file_size_bytes")
    private Long fileSizeBytes;

    @Column(name="sort_order", nullable=false)
    private Integer sortOrder;

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.sortOrder == null) this.sortOrder = 0;
    }
}
