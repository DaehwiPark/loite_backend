package com.boot.loiteBackend.admin.product.brand.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRAND_ID")
    private Long brandId;

    @Column(name = "BRAND_NAME", nullable = false, length = 100)
    private String brandName;

    @Column(name = "BRAND_ORIGIN")
    private String brandOrigin;

    @Column(name = "BRAND_LOGO_URL" )
    private String brandLogoUrl;

    @Column(name = "BRAND_DESCRIPTION")
    private String brandDescription;

    @Column(name = "ACTIVE_YN")
    private String activeYn;

    @Builder.Default
    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N";

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
