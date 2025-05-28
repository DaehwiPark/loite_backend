package com.boot.loiteMsBack.product.product.entity;

import com.boot.loiteMsBack.product.entity.ProductReviewEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_review_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_IMAGE_ID")
    private Long reviewImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID", nullable = false)
    private ProductReviewEntity review;

    @Column(name = "REVIEW_IMAGE_URL", nullable = false, length = 1000)
    private String reviewImageUrl;

    @Column(name = "REVIEW_IMAGE_SORT_ORDER")
    private Integer reviewImageSortOrder;

    @Column(name = "ACTIVE_YN", length = 100)
    private String activeYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
