package com.boot.loiteBackend.domain.home.fullbanner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_home_full_banner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeFullBannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOME_FULL_BANNER_ID",
            columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PK'")
    private Long id;

    @Column(name = "HOME_FULL_BANNER_TITLE", length = 200,
            columnDefinition = "VARCHAR(200) DEFAULT NULL COMMENT '메인 제목'")
    private String title;

    @Column(name = "HOME_FULL_BANNER_SUBTITLE", length = 255,
            columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '보조 문구'")
    private String subtitle;

    @Column(name = "HOME_FULL_BANNER_TITLE_COLOR", length = 7,
            columnDefinition = "CHAR(7) DEFAULT '#FFFFFF' COMMENT '제목 색상 HEX(#RRGGBB)'")
    private String titleColor;

    @Column(name = "HOME_FULL_BANNER_SUBTITLE_COLOR", length = 7,
            columnDefinition = "CHAR(7) DEFAULT '#FFFFFF' COMMENT '보조 문구 색상 HEX(#RRGGBB)'")
    private String subtitleColor;

    @Column(name = "HOME_FULL_BANNER_BG_IMAGE_NAME", length = 255,
            columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '배경 업로드 실제 파일명'")
    private String bgImageName;

    @Column(name = "HOME_FULL_BANNER_BG_IMAGE_URL", length = 1024,
            columnDefinition = "VARCHAR(1024) DEFAULT NULL COMMENT '배경 이미지 URL'")
    private String bgImageUrl;

    @Column(name = "HOME_FULL_BANNER_BG_IMAGE_PATH", length = 500,
            columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT '배경 실제 서버 경로'")
    private String bgImagePath;

    @Column(name = "HOME_FULL_BANNER_BG_IMAGE_SIZE",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '배경 파일 크기(byte)'")
    private Long bgImageSize;

    @Column(name = "HOME_FULL_BANNER_BG_IMAGE_TYPE", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '배경 MIME 타입(image/jpeg 등)'")
    private String bgImageType;

    @Column(name = "HOME_FULL_BANNER_BUTTON_TEXT", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '버튼 텍스트'")
    private String buttonText;

    @Column(name = "HOME_FULL_BANNER_BUTTON_LINK_URL", length = 2048,
            columnDefinition = "VARCHAR(2048) DEFAULT NULL COMMENT '버튼 클릭 시 이동 URL'")
    private String buttonLinkUrl;

    @Column(name = "HOME_FULL_BANNER_BUTTON_LINK_TARGET", length = 10, nullable = false,
            columnDefinition = "VARCHAR(10) NOT NULL DEFAULT '_self' COMMENT '링크 타겟(_self/_blank)'")
    private String buttonLinkTarget;

    @Column(name = "HOME_FULL_BANNER_BUTTON_TEXT_COLOR", length = 7,
            columnDefinition = "CHAR(7) DEFAULT '#FFFFFF' COMMENT '버튼 텍스트 색상'")
    private String buttonTextColor;

    @Column(name = "HOME_FULL_BANNER_BUTTON_BG_COLOR", length = 7,
            columnDefinition = "CHAR(7) DEFAULT '#000000' COMMENT '버튼 배경 색상'")
    private String buttonBgColor;

    @Column(name = "HOME_FULL_BANNER_BUTTON_BORDER_COLOR", length = 7,
            columnDefinition = "CHAR(7) DEFAULT '#FFFFFF' COMMENT '버튼 보더 색상'")
    private String buttonBorderColor;

    @Column(name = "DISPLAY_YN", length = 1, nullable = false,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '노출 여부(Y/N)'")
    private String displayYn;

    @Column(name = "START_AT",
            columnDefinition = "DATETIME DEFAULT NULL COMMENT '노출 시작(옵션)'")
    private LocalDateTime startAt;

    @Column(name = "END_AT",
            columnDefinition = "DATETIME DEFAULT NULL COMMENT '노출 종료(옵션)'")
    private LocalDateTime endAt;

    @Column(name = "SORT_ORDER", nullable = false,
            columnDefinition = "INT(11) NOT NULL DEFAULT 0 COMMENT '노출 순서(낮을수록 상단)'")
    private Integer sortOrder;

    @Column(name = "DEFAULT_YN", length = 1, nullable = false,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '대표 여부(Y/N)'")
    private String defaultYn;

    @Column(name = "CREATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '생성자 USER_ID'")
    private Long createdBy;

    @Column(name = "UPDATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '수정자 USER_ID'")
    private Long updatedBy;

    // DB가 기본값/트리거로 관리
    @Column(name = "CREATED_AT", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;
}
