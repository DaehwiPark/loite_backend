package com.boot.loiteBackend.web.home.hero.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "HomeHeroResponseDto", description = "웹(쇼핑몰) 메인 히어로 응답 DTO")
public class HomeHeroResponseDto {

    @Schema(description = "HOME_HERO_ID", example = "101")
    private Long id;

    @Schema(description = "제목 텍스트", example = "Brew Your Mood, LOITE")
    private String titleText;

    @Schema(description = "본문 텍스트", example = "로이떼의 커피 아틀리에에 초대합니다.")
    private String bodyText;

    @Schema(description = "좌측 이미지 URL")
    private String leftImageUrl;

    @Schema(description = "좌측 이미지 ALT (스키마에는 있으나 DB 컬럼이 없으면 null)")
    private String leftImageAlt;

    @Schema(description = "우측 텍스트 색상", example = "#FFFFFF")
    private String rightTextColor;

    @Schema(description = "우측 배경색", example = "#111111")
    private String rightBgColor;

    @Schema(description = "우측 그라디언트", example = "linear-01")
    private String rightBgGradient;

    @Schema(description = "버튼 텍스트")
    private String buttonText;

    @Schema(description = "버튼 링크")
    private String buttonLink;

    @Schema(description = "버튼 배경색")
    private String buttonBgColor;

    @Schema(description = "버튼 글자색")
    private String buttonTextColor;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y","N"})
    private String displayYn;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer sortOrder;
}
