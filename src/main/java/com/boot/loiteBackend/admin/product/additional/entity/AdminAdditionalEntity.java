package com.boot.loiteBackend.admin.product.additional.entity;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_product_additional")
public class AdminAdditionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDITIONAL_ID")
    private Long additionalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private AdminProductEntity product; // 본상품 참조

    @Column(name = "ADDITIONAL_NAME", nullable = false, length = 100)
    private String additionalName;

    @Column(name = "ADDITIONAL_STOCK", nullable = false)
    private Integer additionalStock;

    @Column(name = "ADDITIONAL_PRICE", nullable = false, precision = 12, scale = 0)
    private BigDecimal additionalPrice;

    @Column(name = "ADDITIONAL_IMAGE_URL", length = 255)
    private String additionalImageUrl;

    @Builder.Default
    @Column(name = "ACTIVE_YN", nullable = false, length = 1)
    private String activeYn = "Y";

    @Builder.Default
    @Column(name = "DELETE_YN", nullable = false, length = 1)
    private String deleteYn = "N";

    @Column(name = "CREATED_AT", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}

