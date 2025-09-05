package com.boot.loiteBackend.domain.home.topbar.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_home_top_bar")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HomeTopBarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOME_TOP_BAR_ID",
            columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PK'")
    private Long id;

    @Column(name = "HOME_TOP_BAR_TEXT", nullable = false, length = 500,
            columnDefinition = "VARCHAR(500) NOT NULL COMMENT '탑바 문구'")
    private String text;

    @Column(name = "HOME_BACKGROUND_COLOR", nullable = false, length = 9,
            columnDefinition = "VARCHAR(9) NOT NULL DEFAULT '#000000' COMMENT '배경색 (#RRGGBB 또는 #RRGGBBAA)'")
    private String backgroundColor;

    @Column(name = "HOME_TEXT_COLOR", nullable = false, length = 9,
            columnDefinition = "VARCHAR(9) NOT NULL DEFAULT '#FFFFFF' COMMENT '텍스트 색상 (#RRGGBB)'")
    private String textColor;

    @Column(name = "DISPLAY_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '노출 여부(Y/N)'")
    private String displayYn;

    @Column(name = "CREATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '생성자 USER_ID'")
    private Long createdBy;

    @Column(name = "UPDATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '수정자 USER_ID'")
    private Long updatedBy;

    @Column(name = "CREATED_AT", nullable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;

    @Column(name = "DEFAULT_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '대표 여부(Y/N)'")
    private String defaultYn;

    // 생성/수정 시각을 앱단에서도 보정하고 싶다면 @PrePersist/@PreUpdate 사용 가능
    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }
}
