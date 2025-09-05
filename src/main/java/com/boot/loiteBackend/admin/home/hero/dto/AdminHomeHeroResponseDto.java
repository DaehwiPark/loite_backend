package com.boot.loiteBackend.admin.home.hero.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeHeroResponseDto", description = "메인 히어로 섹션 응답 DTO")
public class AdminHomeHeroResponseDto {

    @Schema(description = "HOME_HERO_ID", example = "101")
    private Long id;

    @Schema(description = "제목 텍스트", example = "Brew Your Mood, LOITE")
    private String titleText;

    @Schema(description = "본문 텍스트", example = "로이떼의 커피 아틀리에에 초대합니다.")
    private String bodyText;

    @Schema(description = "좌측 이미지 URL", example = "https://cdn.loite.com/home/hero_left.jpg")
    private String leftImageUrl;

    @Schema(description = "좌측 이미지 파일명", example = "hero_left.jpg")
    private String leftImageName;

    @Schema(description = "좌측 이미지 물리 경로", example = "/var/app/uploads/etc/2025/09/uuid_hero_left.jpg")
    private String leftImagePath;

    @Schema(description = "좌측 이미지 크기(byte)", example = "384928")
    private Long leftImageSize;

    @Schema(description = "좌측 이미지 MIME 타입", example = "image/jpeg")
    private String leftImageType;

    @Schema(description = "우측 텍스트 색상(HEX)", example = "#FFFFFF")
    private String rightTextColor;

    @Schema(description = "우측 배경 색상(HEX)", example = "#111111")
    private String rightBgColor;

    @Schema(description = "우측 배경 그라디언트 (프리셋/토큰)", example = "linear")
    private String rightBgGradient;

    @Schema(description = "버튼 텍스트", example = "Shop Now")
    private String buttonText;

    @Schema(description = "버튼 링크", example = "/shop")
    private String buttonLink;

    @Schema(description = "버튼 배경색", example = "#FFFFFF")
    private String buttonBgColor;

    @Schema(description = "버튼 글자색", example = "#000000")
    private String buttonTextColor;

    @Schema(description = "노출 여부", example = "Y")
    private String displayYn;

    @Schema(description = "노출 시작일시", example = "2025-09-02T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시", example = "2025-12-31T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "정렬 순서", example = "1")
    private Integer sortOrder;

    @Schema(description = "생성일시", example = "2025-09-02T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-09-02T12:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 여부", example = "N")
    private String deletedYn;
}
