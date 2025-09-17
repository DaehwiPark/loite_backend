package com.boot.loiteBackend.domain.home.magazinebanner.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_home_magazine_banner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "홈 매거진 배너 엔티티")
public class HomeMagazineBannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOME_MAGAZINE_BANNER_ID")
    @Schema(description = "홈 매거진 배너 고유 ID", example = "1")
    private Long id;

    @Column(name = "PRODUCT_ID",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT 'FK: 상품 ID(선택)'")
    private Long productId;

    @Column(name = "HOME_MAGAZINE_BANNER_VIDEO_NAME", length = 255)
    @Schema(description = "배너 영상 실제 파일명", example = "uuid_video.mp4")
    private String videoName;

    @Column(name = "HOME_MAGAZINE_BANNER_VIDEO_URL", length = 1024)
    @Schema(description = "배너 영상 URL", example = "https://cdn.example.com/video.mp4")
    private String videoUrl;

    @Column(name = "HOME_MAGAZINE_BANNER_VIDEO_PATH", length = 500)
    @Schema(description = "배너 영상 실제 서버 경로", example = "/data/uploads/magazine-banner/video/uuid_video.mp4")
    private String videoPath;

    @Column(name = "HOME_MAGAZINE_BANNER_VIDEO_SIZE")
    @Schema(description = "배너 영상 파일 크기(byte)", example = "1048576")
    private Long videoSize;

    @Column(name = "HOME_MAGAZINE_BANNER_VIDEO_TYPE", length = 100)
    @Schema(description = "배너 영상 MIME 타입", example = "video/mp4")
    private String videoType;

    @Column(name = "HOME_MAGAZINE_BANNER_THUMBNAIL_NAME", length = 255)
    @Schema(description = "배너 썸네일 실제 파일명", example = "uuid_thumb.jpg")
    private String thumbnailName;

    @Column(name = "HOME_MAGAZINE_BANNER_THUMBNAIL_URL", length = 1024)
    @Schema(description = "배너 썸네일 URL", example = "https://cdn.example.com/thumb.jpg")
    private String thumbnailUrl;

    @Column(name = "HOME_MAGAZINE_BANNER_THUMBNAIL_PATH", length = 500)
    @Schema(description = "배너 썸네일 실제 서버 경로", example = "/data/uploads/magazine-banner/thumbnail/uuid_thumb.jpg")
    private String thumbnailPath;

    @Column(name = "HOME_MAGAZINE_BANNER_THUMBNAIL_SIZE")
    @Schema(description = "배너 썸네일 파일 크기(byte)", example = "204800")
    private Long thumbnailSize;

    @Column(name = "HOME_MAGAZINE_BANNER_THUMBNAIL_TYPE", length = 100)
    @Schema(description = "배너 썸네일 MIME 타입", example = "image/jpeg")
    private String thumbnailType;

    @Column(name = "HOME_MAGAZINE_BANNER_TITLE", length = 100, nullable = false)
    @Schema(description = "배너 메인 제목", example = "심플하고 모던한 무드등")
    private String title;

    @Column(name = "HOME_MAGAZINE_BANNER_SUBTITLE", length = 255)
    @Schema(description = "배너 부제목", example = "거실/침실 어디든 잘 어울리는 디자인")
    private String subtitle;

    @Column(name = "HOME_MAGAZINE_BANNER_BUTTON_TEXT", length = 50)
    @Schema(description = "배너 버튼 텍스트", example = "자세히 보기")
    private String buttonText;

    @Column(name = "HOME_MAGAZINE_BANNER_BUTTON_URL", length = 255)
    @Schema(description = "배너 버튼 클릭 시 이동 URL", example = "/magazine/123")
    private String buttonUrl;

    @Column(name = "DISPLAY_YN", length = 1)
    @Schema(description = "노출 여부 (Y/N)", example = "Y")
    private String displayYn = "Y";

    @Column(name = "SORT_ORDER")
    @Schema(description = "배너 정렬 순서", example = "1")
    private Integer sortOrder = 0;

    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    @Column(name = "CREATED_AT",
            nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일'")
    private LocalDateTime createdAt;

    @org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
    @Column(name = "UPDATED_AT",
            nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'")
    private LocalDateTime updatedAt;
}