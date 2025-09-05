package com.boot.loiteBackend.web.home.topbar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(name = "HomeTopBarResponseDto", description = "웹 TopBar 응답 DTO")
public class HomeTopBarResponseDto {

    @Schema(description = "HOME_TOP_BAR_ID", example = "10")
    private Long id;

    @Schema(description = "탑바 문구", example = "즉시 사용 가능 적립금 3000점 증정!")
    private String text;

    @Schema(description = "배경색 #RRGGBB(AA)", example = "#000000")
    private String backgroundColor;

    @Schema(description = "텍스트 색상 #RRGGBB(AA)", example = "#FFFFFF")
    private String textColor;
}
