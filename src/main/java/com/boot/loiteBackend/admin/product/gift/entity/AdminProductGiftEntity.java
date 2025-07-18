package com.boot.loiteBackend.admin.product.gift.entity;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_PRODUCT_GIFT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductGiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_GIFT_ID")
    private Long productGiftId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private AdminProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GIFT_ID", nullable = false)
    private AdminGiftEntity gift;

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
