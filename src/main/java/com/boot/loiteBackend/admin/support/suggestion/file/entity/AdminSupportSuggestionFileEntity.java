package com.boot.loiteBackend.admin.support.suggestion.file.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_suggestion_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSupportSuggestionFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUGGESTION_FILE_ID")
    private Long suggestionFileId;

    @Column(name = "SUGGESTION_ID", nullable = false)
    private Long suggestionId;

    @Column(name = "SUGGESTION_FILE_NAME", nullable = false)
    private String suggestionFileName;

    @Column(name = "SUGGESTION_FILE_URL", nullable = false)
    private String suggestionFileUrl;

    @Column(name = "SUGGESTION_FILE_PATH", nullable = false)
    private String suggestionFilePath;

    @Column(name = "SUGGESTION_FILE_TYPE")
    private String suggestionFileType;

    @Column(name = "SUGGESTION_FILE_SIZE")
    private Integer suggestionFileSize;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
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
}
