package com.boot.loiteBackend.admin.home.fullbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeFullBannerUpdateRequestDto", description = "풀 배너 수정 요청 DTO")
public class AdminHomeFullBannerUpdateRequestDto {

    @Schema(description = "HOME_FULL_BANNER_ID (PK)", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;

    @Schema(description = "메인 제목", example = "가을 시즌 한정 프로모션")
    @Size(max = 200)
    private String title;

    @Schema(description = "보조 문구", example = "따뜻한 커피와 함께")
    @Size(max = 255)
    private String subtitle;

    @Schema(description = "제목 색상(HEX #RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "titleColor는 #RRGGBB 형식이어야 합니다.")
    private String titleColor;

    @Schema(description = "보조 문구 색상(HEX #RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "subtitleColor는 #RRGGBB 형식이어야 합니다.")
    private String subtitleColor;

    @Schema(description = "버튼 텍스트", example = "자세히 보기")
    @Size(max = 100)
    private String buttonText;

    @Schema(description = "버튼 링크 URL", example = "/event/autumn-2025")
    @Size(max = 2048)
    private String buttonLinkUrl;

    @Schema(description = "버튼 링크 타겟", example = "_self", allowableValues = {"_self", "_blank"})
    @Pattern(regexp = "^$|^(_self|_blank)$", message = "buttonLinkTarget은 '_self' 또는 '_blank'만 허용됩니다.")
    private String buttonLinkTarget;

    @Schema(description = "버튼 텍스트 색상(HEX #RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "buttonTextColor는 #RRGGBB 형식이어야 합니다.")
    private String buttonTextColor;

    @Schema(description = "버튼 배경 색상(HEX #RRGGBB)", example = "#000000")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "buttonBgColor는 #RRGGBB 형식이어야 합니다.")
    private String buttonBgColor;

    @Schema(description = "버튼 보더 색상(HEX #RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "buttonBorderColor는 #RRGGBB 형식이어야 합니다.")
    private String buttonBorderColor;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y","N"})
    @Pattern(regexp = "^$|^[YN]$", message = "displayYn은 Y 또는 N 이어야 합니다.")
    private String displayYn;

    @Schema(description = "노출 시작일시(포함)", example = "2025-09-01T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시(포함)", example = "2025-09-30T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서(낮을수록 먼저 노출)", example = "0")
    private Integer sortOrder;

    @Schema(description = "대표 여부", example = "N", allowableValues = {"Y","N"})
    @Pattern(regexp = "^$|^[YN]$", message = "defaultYn은 Y 또는 N 이어야 합니다.")
    private String defaultYn;

    /* 교차 필드 유효성: startAt <= endAt */
    @AssertTrue(message = "startAt은 endAt보다 이후일 수 없습니다.")
    @Schema(hidden = true)
    public boolean isValidDateRange() {
        if (startAt == null || endAt == null) return true;
        return !startAt.isAfter(endAt);
    }
}
