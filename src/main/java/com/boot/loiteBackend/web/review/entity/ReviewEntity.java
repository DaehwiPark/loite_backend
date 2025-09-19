package com.boot.loiteBackend.web.review.entity;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_product_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private AdminProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "ORDER_ITEM_ID")
    private Long orderItemId;

    @Column(name = "REVIEW_RATING", nullable = false)
    private int rating;

    @Column(name = "REVIEW_CONTENT", columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    @Column(name = "ACTIVE_YN", length = 1, nullable = false)
    private String activeYn = "Y";

    @Builder.Default
    @Column(name = "DELETE_YN", length = 1, nullable = false)
    private String deleteYn = "N";

    @Builder.Default
    @Column(name = "HELPFUL_COUNT", nullable = false)
    private int helpfulCount = 0;

    @Column(name = "CREATED_AT", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", insertable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewMediaEntity> medias = new ArrayList<>();
}

