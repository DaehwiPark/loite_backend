package com.boot.loiteBackend.web.home.eventbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "HomeEventBannerResponseDto", description = "웹 배너 응답 DTO (쇼핑몰 노출용)")
public class HomeEventBannerResponseDto {

    @Schema(description = "HOME_BANNER_ID", example = "42")
    private Long id;

    @Schema(description = "배너 제목 (관리용, 화면에는 노출되지 않음)", example = "추석 프로모션 배너")
    private String bannerTitle;

    @Schema(description = "PC용 이미지 URL", example = "https://cdn.loite.com/banner/pc/coffee_festival_2025.jpg")
    private String pcImageUrl;

    @Schema(description = "모바일용 이미지 URL", example = "https://cdn.loite.com/banner/mobile/coffee_festival_2025_m.jpg")
    private String mobileImageUrl;

    @Schema(description = "클릭 시 이동 URL", example = "/event/coffee-festival")
    private String linkUrl;

    @Schema(description = "링크 타겟", example = "_self", allowableValues = {"_self", "_blank"})
    private String linkTarget;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"})
    private String displayYn;

    @Schema(description = "노출 시작일시(포함)", example = "2025-09-01T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시(포함)", example = "2025-09-30T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서(낮을수록 먼저 노출)", example = "0")
    private Integer sortOrder;

    @Schema(description = "대표 슬롯(1/2), 일반 배너는 null", example = "1", nullable = true)
    private Integer defaultSlot;
}
