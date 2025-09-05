package com.boot.loiteBackend.admin.home.topbar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeTopBarCreateRequestDto", description = "탑바 등록 요청 DTO")
public class AdminHomeTopBarCreateRequestDto {

    @Schema(description = "탑바 문구", example = "즉시 사용 가능 적립금 3000점 증정!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 500)
    private String text;

    @Schema(description = "배경색 #RRGGBB(AA)", example = "#000000", defaultValue = "#000000")
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 9)
    private String backgroundColor;

    @Schema(description = "텍스트 색상 #RRGGBB(AA)", example = "#FFFFFF", defaultValue = "#FFFFFF")
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 9)
    private String textColor;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"}, defaultValue = "Y")
    @Pattern(regexp = "^[YN]$")
    private String displayYn;

    @Schema(description = "대표 여부", example = "N", allowableValues = {"Y", "N"}, defaultValue = "N")
    @Pattern(regexp = "^[YN]$")
    private String defaultYn;
}