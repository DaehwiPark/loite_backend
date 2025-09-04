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
@Schema(name = "AdminHomeHeroUpdateRequestDto", description = "메인 히어로 섹션 수정 요청 DTO")
public class AdminHomeHeroUpdateRequestDto {

    @Schema(description = "HOME_HERO_ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;

    @Schema(description = "제목 텍스트", example = "변경된 제목")
    @Size(max = 200)
    private String titleText;

    @Schema(description = "본문 텍스트", example = "변경된 설명")
    @Size(max = 500)
    private String bodyText;

    @Schema(description = "좌측 이미지 URL", example = "https://cdn.loite.com/home/hero_left_new.jpg")
    @Size(max = 500)
    private String leftImageUrl;

    @Schema(description = "좌측 이미지 ALT", example = "변경된 ALT")
    @Size(max = 200)
    private String leftImageAlt;

    @Schema(description = "좌측 파일명", example = "hero_left2.jpg")
    @Size(max = 255)
    private String leftResFileName;

    @Schema(description = "좌측 파일 URL", example = "https://storage.googleapis.com/bucket/hero_left2.jpg")
    @Size(max = 500)
    private String leftResFileUrl;

    @Schema(description = "좌측 파일 경로", example = "/2025/09/hero_left2.jpg")
    @Size(max = 500)
    private String leftResFilePath;

    @Schema(description = "좌측 파일 크기(byte)", example = "500000")
    private Long leftResFileSize;

    @Schema(description = "좌측 파일 타입", example = "image/png")
    @Size(max = 100)
    private String leftResFileType;

    @Schema(description = "우측 텍스트 색상", example = "#FFFFFF")
    @Size(max = 20)
    private String rightTextColor;

    @Schema(description = "우측 배경 색상", example = "#222222")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$",
            message = "rightBgColor는 #RRGGBB 또는 #RRGGBBAA 형식이어야 합니다.")
    @Size(max = 9)
    private String rightBgColor;

    @Schema(description = "우측 그라디언트(코드/키)", example = "linear-1")
    @Size(max = 20)
    private String rightBgGradient;

    @Schema(description = "버튼 텍스트", example = "Learn More")
    @Size(max = 80)
    private String buttonText;

    @Schema(description = "버튼 링크", example = "/about")
    @Size(max = 500)
    private String buttonLink;

    @Schema(description = "버튼 배경색", example = "#000000")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$",
            message = "buttonBgColor는 #RRGGBB 또는 #RRGGBBAA 형식이어야 합니다.")
    @Size(max = 9)
    private String buttonBgColor;

    @Schema(description = "버튼 글자색", example = "#FFFFFF")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$",
            message = "buttonTextColor는 #RRGGBB 또는 #RRGGBBAA 형식이어야 합니다.")
    @Size(max = 9)
    private String buttonTextColor;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y","N"})
    @Pattern(regexp = "^[YN]$", message = "displayYn은 Y 또는 N 이어야 합니다.")
    private String displayYn;

    @Schema(description = "발행 상태", example = "DRAFT", allowableValues = {"DRAFT","PUBLISHED","ARCHIVED"})
    @Pattern(regexp = "^(?i)(DRAFT|PUBLISHED|ARCHIVED)$",
            message = "publishStatus는 DRAFT, PUBLISHED, ARCHIVED 중 하나여야 합니다.")
    @Size(max = 20)
    private String publishStatus;

    @Schema(description = "노출 시작일시", example = "2025-09-10T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시", example = "2025-10-31T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서", example = "2")
    private Integer sortOrder;

    @Schema(description = "수정자 USER_ID", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long updatedBy;
}
