package com.boot.loiteMsBack.policy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_policy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POLICY_ID")
    private Long id;

    @Column(name = "POLICY_TITLE", nullable = false)
    private String title;

    @Column(name = "POLICY_CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "POLICY_VERSION", nullable = false, length = 20)
    private String version;

    @Column(name = "POLICY_IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
