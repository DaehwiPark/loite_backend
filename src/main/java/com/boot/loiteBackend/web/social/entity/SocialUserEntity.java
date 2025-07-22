package com.boot.loiteBackend.web.social.entity;

import com.boot.loiteBackend.web.user.general.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_social", uniqueConstraints = {
        @UniqueConstraint(name = "UK_SOCIAL_TYPE_NUMBER", columnNames = {"SOCIAL_TYPE", "SOCIAL_NUMBER"}),
        @UniqueConstraint(name = "UK_SOCIAL_USER_TYPE", columnNames = {"USER_ID", "SOCIAL_TYPE"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOCIAL_ID", columnDefinition = "bigint(20) NOT NULL AUTO_INCREMENT COMMENT '소셜 연동 고유 ID'")
    private Long socialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_SOCIAL_USER"))
    private UserEntity user;

    @Column(name = "SOCIAL_TYPE", nullable = false, length = 50, columnDefinition = "varchar(50) NOT NULL COMMENT '소셜 제공자 (예: GOOGLE, NAVER, KAKAO)'")
    private String socialType;

    @Column(name = "SOCIAL_NUMBER", nullable = false, length = 255, columnDefinition = "varchar(255) NOT NULL COMMENT '소셜 제공자 측 유저 고유 ID'")
    private String socialNumber;

    @Column(name = "SOCIAL_EMAIL", length = 100, columnDefinition = "varchar(100) DEFAULT NULL COMMENT '소셜 계정 이메일'")
    private String socialEmail;

    @Column(name = "SOCIAL_USER_NAME", length = 50, columnDefinition = "varchar(50) DEFAULT NULL COMMENT '소셜 계정 이름'")
    private String socialUserName;

    @Column(name = "CONNECTED_AT", nullable = false, columnDefinition = "timestamp NOT NULL DEFAULT current_timestamp() COMMENT '소셜 계정 연동 시각'")
    private LocalDateTime connectedAt;

    @PrePersist
    public void prePersist() {
        this.connectedAt = LocalDateTime.now();
    }
}
