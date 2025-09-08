package com.boot.loiteBackend.admin.home.eventbanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeEventBannerResponseDto", description = "배너 응답 DTO(관리자용, 메타 포함)")
public class AdminHomeEventBannerResponseDto {

    @Schema(description = "배너 ID (PK)", example = "101")
    private Long id;

    @Schema(description = "배너 제목(노출 X)", example = "여름맞이 커피 프로모션")
    private String bannerTitle;

    /* PC 이미지 */
    private String pcImageName;
    private String pcImageUrl;
    private String pcImagePath;
    private Long pcImageSize;
    private String pcImageType;

    /* 모바일 이미지 */
    private String mobileImageName;
    private String mobileImageUrl;
    private String mobileImagePath;
    private Long mobileImageSize;
    private String mobileImageType;

    /* 링크/노출 */
    @Schema(description = "클릭 시 이동 URL", example = "/event/special-sale")
    private String linkUrl;

    @Schema(description = "링크 타겟", example = "_blank", allowableValues = {"_self", "_blank"})
    private String linkTarget;

    @Schema(description = "노출 여부", example = "Y", allowableValues = {"Y", "N"})
    private String displayYn;

    @Schema(description = "기본값 여부", example = "Y", allowableValues = {"Y", "N"})
    private String defaultYn;

    @Schema(description = "노출 시작일시")
    private LocalDateTime startAt;

    @Schema(description = "노출 종료일시")
    private LocalDateTime endAt;

    @Schema(description = "대표 슬롯(1/2), 일반 배너는 null", example = "1", nullable = true)
    private Integer defaultSlot;

    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}