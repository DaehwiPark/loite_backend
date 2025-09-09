package com.boot.loiteBackend.admin.home.recommend.section.dto;

import com.boot.loiteBackend.domain.home.recommend.section.entity.HomeRecoSectionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeRecoSectionResponseDto", description = "홈 추천 섹션 응답 DTO")
public class AdminHomeRecoSectionResponseDto {

    @Schema(description = "섹션 ID", example = "1")
    private Long id;

    @Schema(description = "좌측 박스 타이틀", example = "혼자서도 충분히")
    private String title;

    @Schema(description = "이동 URL", example = "/collections/solo")
    private String linkUrl;

    @Schema(description = "링크 타겟", allowableValues = {"_self", "_blank"}, example = "_self")
    private String linkTarget;

    @Schema(description = "노출 여부", allowableValues = {"Y","N"}, example = "Y")
    private String displayYn;

    @Schema(description = "정렬(낮을수록 상단)", example = "0")
    private Integer sortOrder;

    public static AdminHomeRecoSectionResponseDto fromEntity(HomeRecoSectionEntity e) {
        if (e == null) return null;
        return AdminHomeRecoSectionResponseDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .linkUrl(e.getLinkUrl())
                .linkTarget(e.getLinkTarget())
                .displayYn(e.getDisplayYn())
                .sortOrder(e.getSortOrder())
                .build();
    }
}
