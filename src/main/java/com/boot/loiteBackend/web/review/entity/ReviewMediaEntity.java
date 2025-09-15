package com.boot.loiteBackend.web.review.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_review_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewMediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEDIA_ID")
    private Long mediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID", nullable = false)
    private ReviewEntity review;

    @Column(name = "MEDIA_TYPE", nullable = false, length = 20)
    private String mediaType;

    @Column(name = "URL", nullable = false, length = 500)
    private String url;

    @Builder.Default
    @Column(name = "SORT_ORDER", nullable = false)
    private int sortOrder = 0;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}

