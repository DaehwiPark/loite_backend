package com.boot.loiteBackend.domain.user.general.entity;

import com.boot.loiteBackend.domain.social.entity.SocialUserEntity;
import com.boot.loiteBackend.domain.user.role.entity.UserRoleEntity;
import com.boot.loiteBackend.domain.user.status.entity.UserStatusEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", columnDefinition = "BIGINT COMMENT '기본 키'")
    private Long userId;

    @Column(name = "USER_EMAIL", nullable = false, length = 100, columnDefinition = "VARCHAR(100) NOT NULL COMMENT '이메일'")
    private String userEmail;

    @Column(name = "USER_PASSWORD", length = 255, columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '비밀번호 (소셜 로그인 사용자는 NULL)'")
    private String userPassword;

    @Column(name = "USER_NAME", nullable = false, length = 50, columnDefinition = "VARCHAR(50) NOT NULL COMMENT '사용자 이름'")
    private String userName;

    @Column(name = "USER_PHONE", length = 20, columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '휴대폰 번호'")
    private String userPhone;

    @Column(name = "USER_BIRTHDATE", columnDefinition = "DATE DEFAULT NULL COMMENT '생년월일'")
    private LocalDate userBirthdate;

    @Column(name = "USER_REGISTER_TYPE", nullable = false, length = 20, columnDefinition = "VARCHAR(20) NOT NULL DEFAULT 'EMAIL' COMMENT '가입 유형 (EMAIL, KAKAO, NAVER, GOOGLE)'")
    private String userRegisterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ROLE", referencedColumnName = "ROLE_CODE", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_CODE"))
    private UserRoleEntity userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_STATUS", referencedColumnName = "STATUS_CODE", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_STATUS_CODE"))
    private UserStatusEntity userStatus;

    @Column(name = "IS_OVER_14", nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 0 COMMENT '만 14세 이상 여부'")
    private Boolean isOver14;

    @Column(name = "AGREE_TERMS", nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 0 COMMENT '이용약관 동의 여부'")
    private Boolean agreeTerms;

    @Column(name = "AGREE_PRIVACY", nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 0 COMMENT '개인정보 수집 및 이용 동의 여부'")
    private Boolean agreePrivacy;

    @Column(name = "AGREE_MARKETING_SNS", columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '광고/마케팅 알림 수신 동의 여부(SNS)'")
    private Boolean agreeMarketingSns;

    @Column(name = "AGREE_MARKETING_EMAIL", columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '광고/마케팅 알림 수신 동의 여부(EMAIL)'")
    private Boolean agreeMarketingEmail;

    @Column(name = "EMAIL_VERIFIED", nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 0 COMMENT '이메일 인증 여부'")
    private Boolean emailVerified;

    @Column(name = "EMAIL_VERIFIED_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL COMMENT '이메일 인증 시각'")
    private LocalDateTime emailVerifiedAt;

    @Column(name = "LAST_LOGIN_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL COMMENT '마지막 로그인 시각'")
    private LocalDateTime lastLoginAt;

    @Column(name = "WITHDRAWN_AT", columnDefinition = "TIMESTAMP NULL DEFAULT NULL COMMENT '탈퇴 시각'")
    private LocalDateTime withdrawnAt;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각'")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SocialUserEntity> userSocials = new ArrayList<>();
}