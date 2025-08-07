package com.boot.loiteBackend.domain.user.general.entity;

import com.boot.loiteBackend.domain.social.entity.SocialUserEntity;
import com.boot.loiteBackend.domain.user.role.entity.UserRoleEntity;
import com.boot.loiteBackend.domain.user.status.entity.UserStatusEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name = "USER_REGISTER_TYPE", nullable = false, length = 20, columnDefinition = "VARCHAR(20)")
    private String userRegisterType;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ROLE", referencedColumnName = "ROLE_CODE", nullable = false)
    private UserRoleEntity userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_STATUS", referencedColumnName = "STATUS_CODE", nullable = false)
    private UserStatusEntity userStatus;

    @Column(name = "LAST_LOGIN_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private LocalDateTime lastLoginAt;

    @Column(name = "WITHDRAWN_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private LocalDateTime withdrawnAt;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SocialUserEntity> userSocials = new ArrayList<>();

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
