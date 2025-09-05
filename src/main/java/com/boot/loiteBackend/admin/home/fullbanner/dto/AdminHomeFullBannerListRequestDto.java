package com.boot.loiteBackend.admin.home.fullbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeFullBannerListRequestDto", description = "풀 배너 목록 조회 필터")
public class AdminHomeFullBannerListRequestDto {

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"})
    private String displayYn;

    @Schema(description = "검색 키워드(title/subtitle/buttonText/buttonLinkUrl)", example = "가을")
    private String keyword;

    @Schema(description = "노출 시작(이후)", example = "2025-09-01T00:00:00")
    private LocalDateTime startFrom;

    @Schema(description = "노출 종료(이전)", example = "2025-09-30T23:59:59")
    private LocalDateTime endTo;
}
