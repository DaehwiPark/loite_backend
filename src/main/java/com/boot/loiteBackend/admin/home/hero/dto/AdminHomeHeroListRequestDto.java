package com.boot.loiteBackend.admin.home.hero.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeHeroListRequestDto", description = "메인 히어로 섹션 목록 조회 요청 DTO")
public class AdminHomeHeroListRequestDto {

    @Schema(
            description = "발행 상태 필터 (DB: VARCHAR(20))",
            example = "PUBLISHED",
            allowableValues = {"DRAFT","PUBLISHED","ARCHIVED"}
    )
    @Pattern(regexp = "^(?i)(DRAFT|PUBLISHED|ARCHIVED)$", message = "publishStatus는 DRAFT, PUBLISHED, ARCHIVED 중 하나여야 합니다.")
    private String publishStatus;

    @Schema(
            description = "노출 여부 필터 (DB: CHAR(1), Y/N)",
            example = "Y",
            allowableValues = {"Y","N"}
    )
    @Pattern(regexp = "^[YN]$", message = "displayYn은 Y 또는 N 이어야 합니다.")
    private String displayYn;

    @Schema(description = "시작일 필터 (이후, inclusive)", example = "2025-09-01T00:00:00")
    private LocalDateTime startFrom;

    @Schema(description = "종료일 필터 (이전, inclusive)", example = "2025-12-31T23:59:59")
    private LocalDateTime endTo;

    @Schema(description = "페이지 번호 (0부터 시작, 기본값 0)", example = "0", defaultValue = "0")
    @Builder.Default
    @Min(0)
    private Integer page = 0;

    @Schema(description = "페이지 크기 (기본값 10)", example = "10", defaultValue = "10")
    @Builder.Default
    @Min(1)
    private Integer size = 10;
}
