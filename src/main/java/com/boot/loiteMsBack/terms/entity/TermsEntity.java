package com.boot.loiteMsBack.terms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_terms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TERMS_ID")
    private Long id;

    @Column(name = "TERMS_TITLE", nullable = false, length = 255)
    private String title;

    @Column(name = "TERMS_CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "TERMS_VERSION", nullable = false, length = 20)
    private String version;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
