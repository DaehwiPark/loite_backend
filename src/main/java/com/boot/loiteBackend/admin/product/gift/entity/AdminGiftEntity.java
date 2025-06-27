package com.boot.loiteBackend.admin.product.gift.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_GIFT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GIFT_ID")
    private Long giftId;

    @Column(name = "GIFT_NAME", nullable = false, length = 255)
    private String giftName;

    @Column(name = "GIFT_STOCK")
    private Integer giftStock;

    @Column(name = "ACTIVE_YN", length = 1)
    private String activeYn;

    @Column(name = "DELETE_YN", length = 1)
    private String deleteYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.deleteYn == null) this.deleteYn = "N";
        if (this.activeYn == null) this.activeYn = "Y";
        if (this.giftStock == null) this.giftStock = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
