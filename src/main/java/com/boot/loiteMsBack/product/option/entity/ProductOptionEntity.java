package com.boot.loiteMsBack.product.option.entity;

import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import com.boot.loiteMsBack.product.option.enums.OptionStyleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OPTION_ID")
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

    @Column(name = "OPTION_TYPE", length = 100)
    private String optionType;

    @Column(name = "OPTION_VALUE", length = 100)
    private String optionValue;

    @Column(name = "OPTION_ADDITIONAL_PRICE")
    private int optionAdditionalPrice;

    @Column(name = "OPTION_STOCK")
    private int optionStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPTION_STYLE_TYPE", length = 30)
    private OptionStyleType optionStyleType;

    @Column(name = "ACTIVE_YN", length = 100)
    private String activeYn;

    @Column(name = "OPTION_SORT_ORDER")
    private int optionSortOrder;

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
