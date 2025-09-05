package com.boot.loiteBackend.admin.home.hero.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeHeroCreateRequestDto", description = "메인 히어로 섹션 등록 요청 DTO")
public class AdminHomeHeroCreateRequestDto {

    @Schema(description = "제목 텍스트", example = "Brew Your Mood, LOITE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 200)
    private String titleText;

    @Schema(description = "본문 텍스트(서브 카피)", example = "로이떼의 커피 아틀리에에 초대합니다.")
    @Size(max = 500)
    private String bodyText;

    @Schema(description = "좌측 이미지 URL", example = "https://cdn.loite.com/home/hero_left.jpg")
    @Size(max = 500)
    private String leftImageUrl;

    @Schema(description = "좌측 이미지 파일명", example = "hero_left.jpg")
    @Size(max = 255)
    private String leftImageName;

    @Schema(description = "좌측 이미지 물리 경로", example = "/2025/09/hero_left.jpg")
    @Size(max = 500)
    private String leftImagePath;

    @Schema(description = "좌측 이미지 크기(byte)", example = "384928")
    private Long leftImageSize;

    @Schema(description = "좌측 이미지 MIME 타입", example = "image/jpeg")
    @Size(max = 100)
    private String leftImageType;

    @Schema(description = "우측 텍스트 컬러 (#RRGGBB 또는 #RRGGBBAA)", example = "#FFFFFF")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 20)
    private String rightTextColor;

    @Schema(description = "우측 배경 색상 (#RRGGBB 또는 #RRGGBBAA)", example = "#111111", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 9)
    private String rightBgColor;

    @Schema(description = "우측 배경 그라디언트 정의(짧은 토큰/프리셋 키)", example = "linear", maxLength = 20)
    @Size(max = 20)
    private String rightBgGradient;

    /* 버튼 */
    @Schema(description = "버튼 텍스트", example = "Shop Now")
    @Size(max = 80)
    private String buttonText;

    @Schema(description = "버튼 링크(URL)", example = "/shop")
    @Size(max = 500)
    private String buttonLink;

    @Schema(description = "버튼 배경색 (#RRGGBB 또는 #RRGGBBAA)", example = "#FFFFFF")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 9)
    private String buttonBgColor;

    @Schema(description = "버튼 글자색 (#RRGGBB 또는 #RRGGBBAA)", example = "#000000")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 9)
    private String buttonTextColor;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String displayYn;

    @Schema(description = "노출 시작일시", example = "2025-09-02T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시", example = "2025-12-31T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer sortOrder;
}
