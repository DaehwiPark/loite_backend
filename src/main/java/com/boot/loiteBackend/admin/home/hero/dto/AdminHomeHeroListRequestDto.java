package com.boot.loiteBackend.admin.home.hero.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeHeroListRequestDto", description = "메인 히어로 섹션 목록 조회 요청 DTO (필터 전용)")
public class AdminHomeHeroListRequestDto {

    @Schema(description = "노출 여부 필터 (Y/N). 지정하지 않으면 전체 조회", example = "Y", allowableValues = {"Y", "N"})
    private String displayYn;

    @Schema(description = "시작일 필터 (이후, inclusive)", example = "2025-09-01T00:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startFrom;

    @Schema(description = "종료일 필터 (이전, inclusive)", example = "2025-12-31T23:59:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTo;

    @Schema(description = "검색 키워드 (제목/부제/링크)", example = "세일")
    private String keyword;
}
