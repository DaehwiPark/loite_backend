package com.boot.loiteBackend.admin.home.magazinebanner.dto;

import com.boot.loiteBackend.domain.home.magazinebanner.entity.HomeMagazineBannerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class AdminHomeMagazineBannerDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 매거진 배너 요청 DTO")
    public static class Request {
        @Schema(description = "상품 ID", example = "101")
        private Long productId;

        @Schema(description = "배너 영상 URL", example = "https://cdn.example.com/video.mp4")
        private String videoUrl;

        @Schema(description = "배너 영상 썸네일 URL", example = "https://cdn.example.com/thumb.jpg")
        private String videoThumbnailUrl;

        @Schema(description = "배너 제목", example = "심플하고 모던한 무드등")
        private String title;

        @Schema(description = "배너 부제목", example = "거실/침실 어디든 잘 어울리는 디자인")
        private String subtitle;

        @Schema(description = "버튼 텍스트", example = "자세히 보기")
        private String buttonText;

        @Schema(description = "버튼 URL", example = "/magazine/123")
        private String buttonUrl;

        @Schema(description = "노출 여부", example = "Y")
        private String displayYn = "Y";

        @Schema(description = "정렬 순서", example = "1")
        private Integer sortOrder;

        // Request -> Entity
        public static HomeMagazineBannerEntity toEntity(Request dto) {
            if (dto == null) return null;
            return HomeMagazineBannerEntity.builder()
                    .productId(dto.getProductId())
                    .videoUrl(dto.getVideoUrl())
                    .videoThumbnailUrl(dto.getVideoThumbnailUrl())
                    .title(dto.getTitle())
                    .subtitle(dto.getSubtitle())
                    .buttonText(dto.getButtonText())
                    .buttonUrl(dto.getButtonUrl())
                    .displayYn(dto.getDisplayYn())
                    .sortOrder(dto.getSortOrder())
                    .build();
        }

        // Entity 필드 덮어쓰기(수정용)
        public static void apply(HomeMagazineBannerEntity entity, Request dto) {
            if (dto == null || entity == null) return;
            entity.setProductId(dto.getProductId());
            entity.setVideoUrl(dto.getVideoUrl());
            entity.setVideoThumbnailUrl(dto.getVideoThumbnailUrl());
            entity.setTitle(dto.getTitle());
            entity.setSubtitle(dto.getSubtitle());
            entity.setButtonText(dto.getButtonText());
            entity.setButtonUrl(dto.getButtonUrl());
            entity.setDisplayYn(dto.getDisplayYn());
            entity.setSortOrder(dto.getSortOrder());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 매거진 배너 응답 DTO")
    public static class Response {
        private Long id;
        private Long productId;

        private String videoName;
        private String videoUrl;
        private String videoPath;
        private Long videoSize;
        private String videoType;

        private String thumbnailName;
        private String videoThumbnailUrl;
        private String thumbnailPath;
        private Long thumbnailSize;
        private String thumbnailType;

        private String title;
        private String subtitle;
        private String buttonText;
        private String buttonUrl;

        private String displayYn;
        private Integer sortOrder;

        // Entity -> Response
        public static Response fromEntity(HomeMagazineBannerEntity entity) {
            if (entity == null) return null;
            return Response.builder()
                    .id(entity.getId())
                    .productId(entity.getProductId())

                    .videoName(entity.getVideoName())
                    .videoUrl(entity.getVideoUrl())
                    .videoPath(entity.getVideoPath())
                    .videoSize(entity.getVideoSize())
                    .videoType(entity.getVideoType())

                    .thumbnailName(entity.getThumbnailName())
                    .videoThumbnailUrl(entity.getVideoThumbnailUrl())
                    .thumbnailPath(entity.getThumbnailPath())
                    .thumbnailSize(entity.getThumbnailSize())
                    .thumbnailType(entity.getThumbnailType())

                    .title(entity.getTitle())
                    .subtitle(entity.getSubtitle())
                    .buttonText(entity.getButtonText())
                    .buttonUrl(entity.getButtonUrl())
                    .displayYn(entity.getDisplayYn())
                    .sortOrder(entity.getSortOrder())
                    .build();
        }
    }
}