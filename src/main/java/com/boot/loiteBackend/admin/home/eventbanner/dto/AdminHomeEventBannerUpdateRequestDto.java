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
@Schema(name = "AdminHomeEventBannerUpdateRequestDto", description = "배너 수정 요청 DTO")
public class AdminHomeEventBannerUpdateRequestDto {


    @Schema(description = "HOME_EVENT_BANNER_ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;

    @Schema(description = "배너 제목(노출되지 않음)", example = "추석 프로모션 배너")
    @Size(max = 100)
    private String bannerTitle;

    @Schema(description = "링크 URL", example = "/event/coffee")
    @Size(max = 2048)
    private String linkUrl;

    @Schema(description = "링크 타겟", example = "_blank", allowableValues = {"_self", "_blank"})
    private String linkTarget;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"})
    @Pattern(regexp = "Y|N", message = "displayYn은 'Y' 또는 'N'만 허용됩니다.")
    private String displayYn;

    @Schema(description = "기본값 여부", example = "Y", allowableValues = {"Y", "N"})
    @Pattern(regexp = "Y|N", message = "defaultYn은 'Y' 또는 'N'만 허용됩니다.")
    private String defaultYn;

    @Schema(description = "노출 시작일시", example = "2025-09-10T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시", example = "2025-10-31T23:59:59")
    private LocalDateTime endAt;

    @Schema(description = "대표 슬롯(1/2) - 일반이면 null", example = "2")
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
