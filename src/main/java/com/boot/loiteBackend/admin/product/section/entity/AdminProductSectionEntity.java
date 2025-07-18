package com.boot.loiteBackend.admin.product.section.entity;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_PRODUCT_SECTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductSectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECTION_ID")
    private Long sectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private AdminProductEntity product;

    @Column(name = "SECTION_TYPE", length = 50, nullable = false)
    private String sectionType; // DESIGN, DETAIL, FUNCTION, COMPONENT, SPEC 등

    @Lob
    @Column(name = "SECTION_CONTENT", nullable = false)
    private String sectionContent; // HTML or 텍스트 기반 콘텐츠

    @Column(name = "SECTION_ORDER", nullable = false)
    private int sectionOrder; // 출력 순서

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