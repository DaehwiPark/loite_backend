package com.boot.loiteMsBack.support.resource.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_support_resource")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESOURCE_ID")
    private Long resourceId;

    @Column(name = "RESOURCE_PRODUCT_NAME", nullable = false, length = 255)
    private String resourceProductName;

    @Column(name = "RESOURCE_MODEL_NAME", length = 255)
    private String resourceModelName;

    @Column(name = "RESOURCE_FILE_NAME", nullable = false, length = 255)
    private String resourceFileName;

    @Column(name = "RESOURCE_FILE_URL", nullable = false, length = 255)
    private String resourceFileUrl;

    @Column(name = "RESOURCE_FILE_PATH",  nullable = false, length = 1024)
    private String resourceFilePath;

    @Column(name = "RESOURCE_FILE_SIZE")
    private Long resourceFileSize;

    @Column(name = "RESOURCE_FILE_TYPE", length = 255)
    private String resourceFileType;

    @Column (name="DISPLAY_YN", length=1)
    private String displayYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
