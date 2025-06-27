package com.boot.loiteBackend.admin.product.product.entity;

import com.boot.loiteBackend.admin.product.brand.entity.AdminProductBrandEntity;
import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.section.entity.AdminProductSectionEntity;
import com.boot.loiteBackend.admin.product.tag.entity.AdminProductTagEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID")
    private AdminProductBrandEntity productBrand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private AdminProductCategoryEntity productCategory;

    //CREATOR_ID 추가

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminProductImageEntity> productImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminProductOptionEntity> productOptions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminProductTagEntity> productTags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<AdminProductSectionEntity> productSections = new ArrayList<>();

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName;

    @Column(name = "PRODUCT_MODEL_NAME" )
    private String productModelName;

    @Column(name = "PRODUCT_SUMMARY")
    private String productSummary;

    @Builder.Default
    @Column(name = "DELETE_YN")
    private String deleteYn = "N";

    @Column(name = "ACTIVE_YN")
    private String activeYn;

    @Column(name = "main_exposure_yn", columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String mainExposureYn;

    @Column(name = "PRODUCT_PRICE")
    private BigDecimal productPrice;

    @Column(name = "PRODUCT_SUPPLY_PRICE")
    private BigDecimal productSupplyPrice;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "PRODUCT_STOCK")
    private int productStock;

    @Column(name = "RECOMMENDED_YN")
    private String recommendedYn;

    @Column(name = "PRODUCT_DELIVERY_CHARGE")
    private BigDecimal productDeliveryCharge;

    @Column(name = "PRODUCT_FREE_DELIVERY")
    private BigDecimal productFreeDelivery;

    @Column(name = "VIEW_COUNT")
    private int viewCount;

    @Column(name = "SALES_COUNT")
    private int salesCount;

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
