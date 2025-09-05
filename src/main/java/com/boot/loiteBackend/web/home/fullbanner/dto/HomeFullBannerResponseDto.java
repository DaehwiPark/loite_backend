package com.boot.loiteBackend.web.home.fullbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "HomeFullBannerResponseDto", description = "웹 풀 배너 응답 DTO(대표 1건)")
public class HomeFullBannerResponseDto {

    @Schema(description = "HOME_FULL_BANNER_ID", example = "7")
    private Long id;

    @Schema(description = "메인 제목", example = "로이떼 가을 정기세일")
    private String title;

    @Schema(description = "보조 문구", example = "가을 원두 최대 30% 할인")
    private String subtitle;

    @Schema(description = "제목 색상(HEX #RRGGBB)", example = "#FFFFFF")
    private String titleColor;

    @Schema(description = "보조 문구 색상(HEX #RRGGBB)", example = "#DDDDDD")
    private String subtitleColor;

    @Schema(description = "배경 이미지 URL", example = "https://cdn.loite.com/banners/full/autumn_bg.jpg")
    private String bgImageUrl;

    @Schema(description = "버튼 텍스트", example = "지금 구경하기")
    private String buttonText;

    @Schema(description = "버튼 링크 URL", example = "/event/autumn-sale")
    private String buttonLinkUrl;

    @Schema(description = "버튼 링크 타겟", allowableValues = {"_self", "_blank"}, example = "_self")
    private String buttonLinkTarget;

    @Schema(description = "버튼 텍스트 색상(HEX #RRGGBB)", example = "#000000")
    private String buttonTextColor;

    @Schema(description = "버튼 배경 색상(HEX #RRGGBB)", example = "#FFFFFF")
    private String buttonBgColor;

    @Schema(description = "버튼 보더 색상(HEX #RRGGBB)", example = "#FFFFFF")
    private String buttonBorderColor;

    @Schema(description = "노출 시작일시(포함)", example = "2025-09-01T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시(포함)", example = "2025-09-30T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서(낮을수록 먼저)", example = "0")
    private Integer sortOrder;
}
