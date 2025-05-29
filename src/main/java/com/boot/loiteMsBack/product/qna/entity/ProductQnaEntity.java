package com.boot.loiteMsBack.product.qna.entity;

import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import com.boot.loiteMsBack.user.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_qna")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQnaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QNA_ID")
    private Long qnaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "QNA_QUESTION", columnDefinition = "TEXT", nullable = false)
    private String qnaQuestion;

    @Column(name = "QNA_ANSWER", columnDefinition = "TEXT")
    private String qnaAnswer;

    @Column(name = "PRIVATE_YN", length = 100)
    private String privateYn;

    @Column(name = "ANSWERED_YN", length = 100)
    private String answeredYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "ANSWERED_AT")
    private LocalDateTime answeredAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
