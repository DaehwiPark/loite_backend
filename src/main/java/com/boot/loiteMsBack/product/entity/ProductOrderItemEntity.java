package com.boot.loiteMsBack.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long productId;

//    private String brandId;
//    private String creatorId;
//    private int categoryId;

    @Column(name = "PRODUCT_SERIAL_NUMBER", nullable = false, length = 20)
    private String productSerialNumber;

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName;

    @Column(name = "PRODUCT_MODEL_NAME" )
    private String productModelName;

    @Column(name = "PRODUCT_SUMMARY")
    private String productSummary;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "DELETE_YN")
    private String delYn;

    @Column(name = "ACTIVE_YN")
    private String activeYn;

    @Column(name = "PRODUCT_PRICE")
    private int productPrice;

    @Column(name = "PRODUCT_SUPPLY_PRICE")
    private int productSupplyPrice;

    @Column(name = "PRODUCT_STOCK")
    private int productStock;

    @Column(name = "RECOMMENDED_YN")
    private int recommendedYn;

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
