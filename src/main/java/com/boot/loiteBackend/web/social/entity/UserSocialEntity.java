package com.boot.loiteBackend.web.social.entity;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_social",uniqueConstraints = {
                @UniqueConstraint(name = "UK_SOCIAL_TYPE_NUMBER", columnNames = {"SOCIAL_TYPE", "SOCIAL_NUMBER"}),
                @UniqueConstraint(name = "UK_SOCIAL_USER_TYPE", columnNames = {"USER_ID", "SOCIAL_TYPE"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSocialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOCIAL_ID")
    private Long socialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_SOCIAL_USER"))
    private UserEntity user;

    @Column(name = "SOCIAL_TYPE", nullable = false, length = 50)
    private String socialType; // e.g., KAKAO, NAVER, GOOGLE

    @Column(name = "SOCIAL_NUMBER", nullable = false, length = 255)
    private String socialNumber; // e.g., 카카오 ID

    @Column(name = "CONNECTED_AT", nullable = false)
    private LocalDateTime connectedAt;

    @PrePersist
    public void prePersist() {
        this.connectedAt = LocalDateTime.now();
    }
}
