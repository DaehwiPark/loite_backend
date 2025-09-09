package com.boot.loiteBackend.admin.home.recommend.section.dto;

import com.boot.loiteBackend.domain.home.recommend.section.entity.HomeRecoSectionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeRecoSectionDetailResponseDto", description = "홈 추천 섹션 응답 DTO")
public class AdminHomeRecoSectionDetailResponseDto {

    @Schema(description = "섹션 ID", example = "1")
    private Long id;

    @Schema(description = "좌측 박스 타이틀", example = "혼자서도 충분히")
    private String title;

    @Schema(description = "이동 URL", example = "/collections/solo")
    private String linkUrl;

    @Schema(description = "링크 타겟", allowableValues = {"_self", "_blank"}, example = "_self")
    private String linkTarget;

    @Schema(description = "노출 여부", allowableValues = {"Y", "N"}, example = "Y")
    private String displayYn;

    @Schema(description = "정렬(낮을수록 상단)", example = "0")
    private Integer sortOrder;

    @Schema(description = "생성자 USER_ID", example = "1001")
    private Long createdBy;

    @Schema(description = "수정자 USER_ID", example = "1002")
    private Long updatedBy;

    @Schema(description = "생성일시", example = "2025-09-08T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-09-08T11:12:45")
    private LocalDateTime updatedAt;

    public static AdminHomeRecoSectionDetailResponseDto fromEntity(HomeRecoSectionEntity e) {
        if (e == null) return null;
        return AdminHomeRecoSectionDetailResponseDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .linkUrl(e.getLinkUrl())
                .linkTarget(e.getLinkTarget())
                .displayYn(e.getDisplayYn())
                .sortOrder(e.getSortOrder())
                .createdBy(e.getCreatedBy())
                .updatedBy(e.getUpdatedBy())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
