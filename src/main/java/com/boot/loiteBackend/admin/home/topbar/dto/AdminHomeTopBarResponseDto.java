package com.boot.loiteBackend.admin.home.topbar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeTopBarResponseDto", description = "탑바 응답 DTO")
public class AdminHomeTopBarResponseDto {

    @Schema(description = "HOME_TOP_BAR_ID", example = "10")
    private Long id;

    @Schema(description = "탑바 문구", example = "즉시 사용 가능 적립금 3000점 증정!")
    private String text;

    @Schema(description = "배경색", example = "#000000")
    private String backgroundColor;

    @Schema(description = "텍스트 색상", example = "#FFFFFF")
    private String textColor;

    @Schema(description = "노출 여부", example = "Y")
    private String displayYn;

    @Schema(description = "대표 여부", example = "Y")
    private String defaultYn;

    @Schema(description = "생성자", example = "1")
    private Long createdBy;

    @Schema(description = "수정자", example = "2")
    private Long updatedBy;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
}
