package com.boot.loiteBackend.product.review.entity;

import com.boot.loiteBackend.product.product.entity.ProductEntity;
import com.boot.loiteBackend.user.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "REVIEW_RATING", nullable = false)
    private Integer reviewRating;

    @Column(name = "REVIEW_CONTENT", columnDefinition = "TEXT")
    private String reviewContent;

    @Column(name = "ACTIVE_YN", length = 100)
    private String activeYn;

    @Column(name = "HELPFUL_COUNT")
    private Integer helpfulCount;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.helpfulCount == null) {
            this.helpfulCount = 0;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
