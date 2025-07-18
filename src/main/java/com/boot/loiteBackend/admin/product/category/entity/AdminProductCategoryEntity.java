package com.boot.loiteBackend.admin.product.category.entity;

import com.boot.loiteBackend.admin.product.brand.entity.AdminProductBrandEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tb_product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    // 자기 자신을 참조하는 상위 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_PARENT_ID")
    private AdminProductCategoryEntity categoryParentId;

    // 자식 카테고리 목록 (양방향 매핑 시 사용)
    @Builder.Default
    @OneToMany(mappedBy = "categoryParentId")
    private List<AdminProductCategoryEntity> childCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID")
    private AdminProductBrandEntity productBrand;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "CATEGORY_DEPTH")
    private Integer categoryDepth;

    @Column(name = "CATEGORY_SORT_ORDER")
    private int categorySortOrder;

    @Column(name = "ACTIVE_YN")
    private String activeYn;

    @Builder.Default
    @Column(name = "DELETE_YN")
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

