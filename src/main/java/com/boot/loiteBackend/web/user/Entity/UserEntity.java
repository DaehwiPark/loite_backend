package com.boot.loiteBackend.web.user.entity;

import com.boot.loiteBackend.web.social.entity.UserSocialEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", columnDefinition = "BIGINT")
    private Long userId;

    @Column(name = "USER_EMAIL", nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String userEmail;

    @Column(name = "USER_PASSWORD", length = 255, columnDefinition = "VARCHAR(255)")
    private String userPassword;

    @Column(name = "USER_NAME", nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
    private String userName;

    @Column(name = "USER_PHONE", nullable = false, length = 20, columnDefinition = "VARCHAR(20)")
    private String userPhone;

    @Column(name = "USER_BIRTHDATE", nullable = false, columnDefinition = "DATE")
    private LocalDate userBirthdate;

    @Column(name = "IS_OVER_14", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isOver14;

    @Column(name = "AGREE_TERMS", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean agreeTerms;

    @Column(name = "AGREE_PRIVACY", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean agreePrivacy;

    @Column(name = "AGREE_MARKETING_SNS", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean agreeMarketingSns;

    @Column(name = "AGREE_MARKETING_EMAIL", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean agreeMarketingEmail;

    @Column(name = "EMAIL_VERIFIED", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean emailVerified;

    @Column(name = "EMAIL_VERIFIED_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private LocalDateTime emailVerifiedAt;

    @Column(name = "ROLE", nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    private String role;

    @Column(name = "USER_STATUS", nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
    private String userStatus;

    @Column(name = "LAST_LOGIN_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private LocalDateTime lastLoginAt;

    @Column(name = "WITHDRAWN_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private LocalDateTime withdrawnAt;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserSocialEntity> userSocials = new ArrayList<>();

}
