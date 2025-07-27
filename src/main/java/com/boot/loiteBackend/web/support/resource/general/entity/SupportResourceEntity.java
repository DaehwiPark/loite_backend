package com.boot.loiteBackend.web.support.resource.general.entity;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
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
    @Column(name = "RESOURCE_ID", columnDefinition = "BIGINT COMMENT '기본 키'")
    private Long resourceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "PRODUCT_ID",
            referencedColumnName = "PRODUCT_ID",
            foreignKey = @ForeignKey(name = "FK_RESOURCE_PRODUCT")
    )
    private AdminProductEntity product;

    @Column(
            name = "RESOURCE_FILE_NAME",
            nullable = false,
            length = 255,
            columnDefinition = "VARCHAR(255) COMMENT '업로드된 실제 파일명'"
    )
    private String resourceFileName;

    @Column(
            name = "RESOURCE_FILE_URL",
            nullable = false,
            length = 255,
            columnDefinition = "VARCHAR(255) COMMENT '업로드 경로'"
    )
    private String resourceFileUrl;

    @Column(
            name = "RESOURCE_FILE_PATH",
            length = 1024,
            columnDefinition = "VARCHAR(1024) COMMENT '실제 서버 내 파일 경로'"
    )
    private String resourceFilePath;

    @Column(
            name = "RESOURCE_FILE_SIZE",
            columnDefinition = "BIGINT COMMENT '파일 크기 (byte 단위)'"
    )
    private Long resourceFileSize;

    @Column(
            name = "RESOURCE_FILE_TYPE",
            length = 255,
            columnDefinition = "VARCHAR(255) COMMENT '파일 타입'"
    )
    private String resourceFileType;

    @Column(
            name = "DISPLAY_YN",
            nullable = false,
            length = 1,
            columnDefinition = "CHAR(1) DEFAULT 'Y' COMMENT '자료 노출 여부 (Y: 노출, N: 비노출)'"
    )
    private String displayYn;

    @Column(
            name = "CREATED_AT",
            columnDefinition = "DATETIME(6) COMMENT '생성일'"
    )
    private LocalDateTime createdAt;

    @Column(
            name = "UPDATED_AT",
            columnDefinition = "DATETIME(6) COMMENT '수정일'"
    )
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