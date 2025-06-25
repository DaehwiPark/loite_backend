package com.boot.loiteBackend.admin.product.product.entity;

import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

    @Column(name = "IMAGE_URL", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "IMAGE_PATH", nullable = false, length = 1000)
    private String imagePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "IMAGE_TYPE", nullable = false, length = 20)
    private ImageType imageType;

    @Column(name = "IMAGE_SORT_ORDER")
    private int imageSortOrder;

    @Column(name = "ACTIVE_YN", length = 100)
    private String activeYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

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
