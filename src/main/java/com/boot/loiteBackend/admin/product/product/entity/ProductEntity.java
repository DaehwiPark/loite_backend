package com.boot.loiteBackend.admin.product.product.entity;

import com.boot.loiteBackend.admin.product.brand.entity.ProductBrandEntity;
import com.boot.loiteBackend.admin.product.category.entity.ProductCategoryEntity;
import com.boot.loiteBackend.admin.product.option.entity.ProductOptionEntity;
import com.boot.loiteBackend.admin.product.tag.entity.ProductTagEntity;
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
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID")
    private ProductBrandEntity productBrand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private ProductCategoryEntity productCategory;

    //CREATOR_ID 추가

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageEntity> productImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionEntity> productOptions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTagEntity> productTags = new ArrayList<>();

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName;

    @Column(name = "PRODUCT_MODEL_NAME" )
    private String productModelName;

    @Column(name = "PRODUCT_SUMMARY")
    private String productSummary;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Builder.Default
    @Column(name = "DELETE_YN")
    private String deleteYn = "N";

    @Column(name = "ACTIVE_YN")
    private String activeYn;

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
