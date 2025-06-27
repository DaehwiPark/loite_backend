package com.boot.loiteBackend.admin.product.discount.entity;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.discount.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_discount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductDiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISCOUNT_ID")
    private Long discountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private AdminProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCOUNT_TYPE", nullable = false, length = 20)
    private DiscountType discountType;

    @Column(name = "DISCOUNT_VALUE", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "START_DATE", nullable = false)
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "ACTIVE_YN")
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
