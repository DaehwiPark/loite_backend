package com.boot.loiteMsBack.support.counsel.entity;

import com.boot.loiteMsBack.support.counsel.enums.CounselStatus;
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
public class SupportCounselEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNSEL_ID")
    private Long counselId;

    @Column(name = "COUNSEL_USER_ID", nullable = false)
    private Long counselUserId;

    @Column(name = "COUNSEL_TITLE", nullable = false)
    private String counselTitle;

    @Column(name = "COUNSEL_CONTENT", nullable = false)
    private String counselContent;

    @Column(name = "COUNSEL_EMAIL")
    private String counselEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "COUNSEL_STATUS")
    private CounselStatus counselStatus;

    @Column(name = "COUNSEL_REPLY_CONTENT")
    private String counselReplyContent;

    @Column(name = "COUNSEL_REPLIED_AT")
    private LocalDateTime counselRepliedAt;

    @Column(name = "COUNSEL_REPLIED_BY")
    private Long counselRepliedBy;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "DEL_YN", nullable = false)
    private String delYn;

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
