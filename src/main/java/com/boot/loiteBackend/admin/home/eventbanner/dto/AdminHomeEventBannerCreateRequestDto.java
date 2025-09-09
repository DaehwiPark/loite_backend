package com.boot.loiteBackend.admin.home.eventbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeEventBannerCreateRequestDto", description = "홈 메인 혜택 배너 등록 요청 DTO")
public class AdminHomeEventBannerCreateRequestDto {

    @Schema(description = "배너 제목(노출 X)", example = "여름맞이 커피 프로모션")
    @Size(max = 100)
    private String bannerTitle;

    @Schema(description = "클릭 시 이동 URL", example = "/event/coffee")
    @Size(max = 2048)
    private String linkUrl;

    @Schema(description = "링크 타겟", example = "_self", allowableValues = {"_self", "_blank"})
    private String linkTarget;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"})
    @Pattern(regexp = "Y|N", message = "displayYn은 'Y' 또는 'N'만 허용됩니다.")
    private String displayYn;

    @Schema(description = "기본값 여부", example = "Y", allowableValues = {"Y", "N"})
    @Pattern(regexp = "Y|N", message = "defaultYn은 'Y' 또는 'N'만 허용됩니다.")
    private String defaultYn;

    @Schema(description = "노출 시작일시", example = "2025-09-01T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시", example = "2025-10-01T00:00:00")
    private LocalDateTime endAt;

    @Schema(description = "대표 슬롯(1/2) - 일반이면 null", example = "1", nullable = true)
    @Min(value = 1, message = "defaultSlot은 1 또는 2만 허용됩니다.")
    @Max(value = 2, message = "defaultSlot은 1 또는 2만 허용됩니다.")
    private Integer defaultSlot;

    @AssertTrue(message = "startAt은 endAt보다 이후일 수 없습니다.")
    @Schema(hidden = true)
    public boolean isValidDateRange() {
        if (startAt == null || endAt == null) return true;
        return !startAt.isAfter(endAt);
    }
}
