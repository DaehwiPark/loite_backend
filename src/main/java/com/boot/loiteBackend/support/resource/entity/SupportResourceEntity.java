package com.boot.loiteBackend.support.resource.entity;

import com.boot.loiteBackend.product.product.entity.ProductEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    private ProductEntity product;

    @Column(name = "RESOURCE_FILE_NAME", nullable = false, length = 255)
    private String resourceFileName;

    @Column(name = "RESOURCE_FILE_URL", nullable = false, length = 255)
    private String resourceFileUrl;

    @Column(name = "RESOURCE_FILE_PATH", length = 1024)
    private String resourceFilePath;

    @Column(name = "RESOURCE_FILE_SIZE")
    private Long resourceFileSize;

    @Column(name = "RESOURCE_FILE_TYPE", length = 255)
    private String resourceFileType;

    @Column(name = "DISPLAY_YN", length = 1, nullable = false)
    private String displayYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
