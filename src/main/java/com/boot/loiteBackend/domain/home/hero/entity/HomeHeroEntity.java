package com.boot.loiteBackend.domain.home.hero.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_home_hero")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class HomeHeroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOME_HERO_ID",
            columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PK'")
    private Long id;

    @Column(name = "HOME_HERO_LEFT_IMAGE_NAME", length = 255,
            columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '업로드된 실제 파일명'")
    private String leftImageName;

    @Column(name = "HOME_HERO_LEFT_IMAGE_URL", nullable = false, length = 500,
            columnDefinition = "VARCHAR(500) NOT NULL COMMENT '좌측 이미지 URL'")
    private String leftImageUrl;

    @Column(name = "HOME_HERO_LEFT_IMAGE_PATH", length = 500,
            columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT '실제 서버 내 파일 경로'")
    private String leftImagePath;

    @Column(name = "HOME_HERO_LEFT_IMAGE_SIZE",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '파일 크기(byte)'")
    private Long leftImageSize;

    @Column(name = "HOME_HERO_LEFT_IMAGE_TYPE", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT 'MIME 타입(image/jpeg 등)'")
    private String leftImageType;

    /* ===== 우측 영역 스타일 ===== */
    @Column(name = "HOME_HERO_RIGHT_TEXT_COLOR", length = 20,
            columnDefinition = "VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '텍스트 컬러'")
    private String rightTextColor;

    @Column(name = "HOME_HERO_RIGHT_BG_COLOR", nullable = false, length = 9,
            columnDefinition = "VARCHAR(9) NOT NULL DEFAULT '#111111' COMMENT '#RRGGBB 또는 #RRGGBBAA'")
    private String rightBgColor;

    @Column(name = "HOME_HERO_RIGHT_BG_GRADIENT", length = 20,
            columnDefinition = "VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '그라디언트 정의(JSON)'")
    private String rightBgGradient;

    @Column(name = "HOME_HERO_TITLE_TEXT", nullable = false, length = 200,
            columnDefinition = "VARCHAR(200) NOT NULL COMMENT '제목 텍스트'")
    private String titleText;

    @Column(name = "HOME_HERO_BODY_TEXT", length = 500,
            columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT '본문 텍스트(서브 카피)'")
    private String bodyText;

    @Column(name = "HOME_HERO_BUTTON_LINK", length = 500,
            columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT '버튼 링크(URL)'")
    private String buttonLink;

    @Column(name = "HOME_HERO_BUTTON_BG_COLOR", length = 9,
            columnDefinition = "VARCHAR(9) DEFAULT NULL COMMENT '버튼 배경색'")
    private String buttonBgColor;

    @Column(name = "HOME_HERO_BUTTON_TEXT", length = 80,
            columnDefinition = "VARCHAR(80) DEFAULT NULL COMMENT '버튼 텍스트'")
    private String buttonText;

    @Column(name = "HOME_HERO_BUTTON_TEXT_COLOR", length = 9,
            columnDefinition = "VARCHAR(9) DEFAULT NULL COMMENT '버튼 글자색'")
    private String buttonTextColor;

    @Column(name = "START_AT",
            columnDefinition = "DATETIME DEFAULT NULL COMMENT '노출 시작일시'")
    private LocalDateTime startAt;

    @Column(name = "END_AT",
            columnDefinition = "DATETIME DEFAULT NULL COMMENT '노출 종료일시'")
    private LocalDateTime endAt;

    @Column(name = "SORT_ORDER", nullable = false,
            columnDefinition = "INT(11) NOT NULL DEFAULT 1 COMMENT '정렬 순서(여러 섹션 운용 시)'")
    private Integer sortOrder;

    @Column(name = "DISPLAY_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '노출 여부'")
    private String displayYn;

    @Column(name = "CREATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '생성자 USER_ID'")
    private Long createdBy;

    @Column(name = "UPDATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '수정자 USER_ID'")
    private Long updatedBy;

    @Column(name = "CREATED_AT", nullable = false, updatable = false, insertable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;

    @Column(name = "DELETED_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '삭제 여부'")
    private String deletedYn;
}
