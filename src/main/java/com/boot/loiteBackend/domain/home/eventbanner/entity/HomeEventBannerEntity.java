package com.boot.loiteBackend.domain.home.eventbanner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_home_event_banner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeEventBannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOME_EVENT_BANNER_ID",
            columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PK'")
    private Long id;

    @Column(name = "HOME_EVENT_BANNER_TITLE", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '배너 제목 (노출 X)'")
    private String bannerTitle;

    @Column(name = "HOME_EVENT_BANNER_PC_IMAGE_NAME", length = 255,
            columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT 'PC용 업로드된 실제 파일명'")
    private String pcImageName;

    @Column(name = "HOME_EVENT_BANNER_PC_IMAGE_URL", length = 1024,
            columnDefinition = "VARCHAR(1024) DEFAULT NULL COMMENT 'PC용 이미지 경로/URL'")
    private String pcImageUrl;

    @Column(name = "HOME_EVENT_BANNER_PC_IMAGE_PATH", length = 500,
            columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT 'PC용 실제 서버 내 파일 경로'")
    private String pcImagePath;

    @Column(name = "HOME_EVENT_BANNER_PC_IMAGE_SIZE",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT 'PC용 파일 크기(byte)'")
    private Long pcImageSize;

    @Column(name = "HOME_EVENT_BANNER_PC_IMAGE_TYPE", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT 'PC용 MIME 타입(image/jpeg 등)'")
    private String pcImageType;

    @Column(name = "HOME_EVENT_BANNER_MOBILE_IMAGE_NAME", length = 255,
            columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '모바일용 업로드된 실제 파일명'")
    private String mobileImageName;

    @Column(name = "HOME_EVENT_BANNER_MOBILE_IMAGE_URL", length = 1024,
            columnDefinition = "VARCHAR(1024) DEFAULT NULL COMMENT '모바일용 이미지 경로/URL'")
    private String mobileImageUrl;

    @Column(name = "HOME_EVENT_BANNER_MOBILE_IMAGE_PATH", length = 500,
            columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT '모바일용 실제 서버 내 파일 경로'")
    private String mobileImagePath;

    @Column(name = "HOME_EVENT_BANNER_MOBILE_IMAGE_SIZE",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '모바일용 파일 크기(byte)'")
    private Long mobileImageSize;

    @Column(name = "HOME_EVENT_BANNER_MOBILE_IMAGE_TYPE", length = 100,
            columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '모바일용 MIME 타입(image/jpeg 등)'")
    private String mobileImageType;

    @Column(name = "HOME_EVENT_BANNER_LINK_URL", length = 2048,
            columnDefinition = "VARCHAR(2048) DEFAULT NULL COMMENT '클릭 시 이동 URL'")
    private String linkUrl;

    @Column(name = "HOME_EVENT_BANNER_LINK_TARGET", length = 10, nullable = false,
            columnDefinition = "VARCHAR(10) NOT NULL DEFAULT '_self' COMMENT '링크 타겟(_self/_blank 등)'")
    private String linkTarget;

    @Column(name = "DISPLAY_YN", length = 1, nullable = false,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '노출 여부(Y/N)'")
    private String displayYn;

    @Column(name = "DEFAULT_YN", length = 1, nullable = false,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '기본 값 (Y/N)'")
    private String defaultYn;

    @Column(name = "START_AT",
            columnDefinition = "DATETIME DEFAULT NULL COMMENT '노출 시작일시(옵션)'")
    private LocalDateTime startAt;

    @Column(name = "END_AT",
            columnDefinition = "DATETIME DEFAULT NULL COMMENT '노출 종료일시(옵션)'")
    private LocalDateTime endAt;

    @Column(name = "DEFAULT_SLOT",
            columnDefinition = "TINYINT(4) DEFAULT NULL COMMENT '대표 슬롯(1/2), NULL=일반'")
    private Integer defaultSlot;

    @Column(name = "CREATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '생성자 USER_ID'")
    private Long createdBy;

    @Column(name = "UPDATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '수정자 USER_ID'")
    private Long updatedBy;

    @Column(name = "CREATED_AT", nullable = false, updatable = false, insertable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, updatable = false, insertable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;
}
