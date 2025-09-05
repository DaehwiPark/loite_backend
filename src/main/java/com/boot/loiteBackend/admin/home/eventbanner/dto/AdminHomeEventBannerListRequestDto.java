package com.boot.loiteBackend.admin.home.eventbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeEventBannerListRequestDto", description = "배너 목록 조회 필터")
public class AdminHomeEventBannerListRequestDto {

    @Schema(description = "배너 제목 (검색용, 부분일치)", example = "추석 프로모션")
    private String bannerTitle;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y","N"})
    @Pattern(regexp = "Y|N", message = "displayYn은 'Y' 또는 'N'만 허용됩니다.")
    private String displayYn;

    @Schema(description = "노출 시작일(이후, inclusive)", example = "2025-09-01T00:00:00")
    private LocalDateTime startFrom;

    @Schema(description = "노출 종료일(이전, inclusive)", example = "2025-10-31T23:59:59")
    private LocalDateTime endTo;

    @Schema(description = "페이지 번호(0부터 시작)", example = "0", defaultValue = "0")
    @Builder.Default
    @Min(value = 0, message = "page는 0 이상이어야 합니다.")
    private Integer page = 0;

    @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
    @Builder.Default
    @Min(value = 1, message = "size는 1 이상이어야 합니다.")
    private Integer size = 10;

    @AssertTrue(message = "startFrom은 endTo보다 이후일 수 없습니다.")
    @Schema(hidden = true)
    public boolean isValidDateRange() {
        if (startFrom == null || endTo == null) return true;
        return !startFrom.isAfter(endTo);
    }
}