package com.boot.loiteBackend.admin.home.fullbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(name = "AdminHomeFullBannerCreateRequestDto", description = "풀 배너 등록 요청 DTO")
public class AdminHomeFullBannerCreateRequestDto {

    @Schema(description = "메인 제목", example = "로이떼 풀 배너")
    @Size(max = 200)
    private String title;

    @Schema(description = "보조 문구", example = "가을 한정 이벤트")
    @Size(max = 255)
    private String subtitle;

    @Schema(description = "제목 색상(#RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String titleColor;

    @Schema(description = "보조 문구 색상(#RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String subtitleColor;

    @Schema(description = "버튼 텍스트", example = "자세히 보기")
    @Size(max = 100)
    private String buttonText;

    @Schema(description = "버튼 링크 URL", example = "/event/autumn")
    @Size(max = 2048)
    private String buttonLinkUrl;

    @Schema(description = "링크 타겟", example = "_self", allowableValues = {"_self","_blank"})
    @Pattern(regexp = "_self|_blank")
    private String buttonLinkTarget;

    @Schema(description = "버튼 텍스트 색상(#RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String buttonTextColor;

    @Schema(description = "버튼 배경 색상(#RRGGBB)", example = "#000000")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String buttonBgColor;

    @Schema(description = "버튼 보더 색상(#RRGGBB)", example = "#FFFFFF")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String buttonBorderColor;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y","N"})
    @Pattern(regexp = "Y|N")
    private String displayYn;

    @Schema(description = "노출 시작일시", example = "2025-09-01T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시", example = "2025-09-30T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서(낮을수록 상단)", example = "0")
    @Min(0)
    private Integer sortOrder;

    @Schema(description = "대표 여부(Y/N)", example = "N", allowableValues = {"Y","N"})
    @Pattern(regexp = "Y|N")
    private String defaultYn;

    @AssertTrue(message = "startAt은 endAt보다 이후일 수 없습니다.")
    @Schema(hidden = true)
    public boolean isValidDateRange() {
        if (startAt == null || endAt == null) return true;
        return !startAt.isAfter(endAt);
    }
}
