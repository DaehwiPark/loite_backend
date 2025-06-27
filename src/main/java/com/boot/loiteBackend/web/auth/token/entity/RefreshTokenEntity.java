package com.boot.loiteBackend.web.auth.token.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenEntity {

    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "REFRESH_TOKEN", nullable = false, length = 512)
    private String refreshToken;

    @Column(name = "EXPIRY_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiryDate;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "REFRESHED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime refreshedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.refreshedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.refreshedAt = LocalDateTime.now();
    }
}
