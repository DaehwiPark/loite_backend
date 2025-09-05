package com.boot.loiteBackend.admin.home.topbar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeTopBarUpdateRequestDto", description = "탑바 수정 요청 DTO")
public class AdminHomeTopBarUpdateRequestDto {

    @Schema(description = "HOME_TOP_BAR_ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;

    @Schema(description = "탑바 문구", example = "문구 변경")
    @Size(max = 500)
    private String text;

    @Schema(description = "배경색 #RRGGBB(AA)", example = "#111111")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 9)
    private String backgroundColor;

    @Schema(description = "텍스트 색상 #RRGGBB(AA)", example = "#FFFFFF")
    @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?$")
    @Size(max = 9)
    private String textColor;

    @Schema(description = "노출 여부", example = "N", allowableValues = {"Y", "N"})
    @Pattern(regexp = "^$|^[YN]$")
    private String displayYn;

    @Schema(description = "대표 여부", example = "Y", allowableValues = {"Y", "N"})
    @Pattern(regexp = "^$|^[YN]$")
    private String defaultYn;

}