package com.boot.loiteBackend.admin.product.additional.entity;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_product_additional")
public class AdminProductAdditionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ADDITIONAL_ID")
    private Long productAdditionalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private AdminProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDITIONAL_ID", nullable = false)
    private AdminAdditionalEntity additional;

    @Column(name = "DELETE_YN")
    private String deleteYn;

    @Column(name = "CREATED_AT", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
