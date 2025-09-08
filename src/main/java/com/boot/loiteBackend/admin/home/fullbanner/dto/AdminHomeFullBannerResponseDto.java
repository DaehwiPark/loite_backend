package com.boot.loiteBackend.admin.home.fullbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeFullBannerResponseDto", description = "풀 배너 응답 DTO(관리자용)")
public class AdminHomeFullBannerResponseDto {

    @Schema(description = "HOME_FULL_BANNER_ID (PK)", example = "12")
    private Long id;

    @Schema(description = "메인 제목", example = "가을 시즌 한정 프로모션")
    private String title;

    @Schema(description = "보조 문구", example = "따뜻한 커피와 함께")
    private String subtitle;

    @Schema(description = "제목 색상(HEX #RRGGBB)", example = "#FFFFFF")
    private String titleColor;

    @Schema(description = "보조 문구 색상(HEX #RRGGBB)", example = "#FFFFFF")
    private String subtitleColor;

    @Schema(description = "배경 업로드 실제 파일명", example = "full_bg_2025.jpg", nullable = true)
    private String bgImageName;

    @Schema(description = "배경 이미지 URL", example = "https://cdn.loite.com/full/banner/full_bg_2025.jpg", nullable = true)
    private String bgImageUrl;

    @Schema(description = "배경 실제 서버 경로", example = "/uploads/fullbanner/2025/full_bg_2025.jpg", nullable = true)
    private String bgImagePath;

    @Schema(description = "배경 파일 크기(byte)", example = "1048576", nullable = true)
    private Long bgImageSize;

    @Schema(description = "배경 MIME 타입", example = "image/jpeg", nullable = true)
    private String bgImageType;

    @Schema(description = "버튼 텍스트", example = "자세히 보기", nullable = true)
    private String buttonText;

    @Schema(description = "버튼 링크 URL", example = "/event/autumn-2025", nullable = true)
    private String buttonLinkUrl;

    @Schema(description = "버튼 링크 타겟", example = "_self", allowableValues = {"_self", "_blank"})
    private String buttonLinkTarget;

    @Schema(description = "버튼 텍스트 색상(HEX #RRGGBB)", example = "#FFFFFF", nullable = true)
    private String buttonTextColor;

    @Schema(description = "버튼 배경 색상(HEX #RRGGBB)", example = "#000000", nullable = true)
    private String buttonBgColor;

    @Schema(description = "버튼 보더 색상(HEX #RRGGBB)", example = "#FFFFFF", nullable = true)
    private String buttonBorderColor;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"})
    private String displayYn;

    @Schema(description = "노출 시작일시(포함)", example = "2025-09-01T00:00:00", nullable = true)
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시(포함)", example = "2025-09-30T23:59:59", nullable = true)
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서(낮을수록 먼저 노출)", example = "0")
    private Integer sortOrder;

    @Schema(description = "대표 여부", example = "N", allowableValues = {"Y", "N"})
    private String defaultYn;

    @Schema(description = "생성자 USER_ID", example = "1001", nullable = true)
    private Long createdBy;

    @Schema(description = "수정자 USER_ID", example = "1002", nullable = true)
    private Long updatedBy;

    @Schema(description = "생성일시", example = "2025-09-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-09-01T12:00:00")
    private LocalDateTime updatedAt;
}
