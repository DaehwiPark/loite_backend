package com.boot.loiteBackend.admin.home.recommend.section.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeRecoSectionCreateDto", description = "홈 추천 섹션 생성 요청 DTO")

public class AdminHomeRecoSectionCreateDto {
    @Schema(description = "좌측 박스 타이틀", example = "혼자서도 충분히", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String title;

    @Schema(description = "이동 URL", example = "/collections/solo")
    private String linkUrl;

    @Schema(description = "링크 타겟", allowableValues = {"_self", "_blank"}, example = "_self")
    private String linkTarget;

    @Schema(description = "노출 여부", allowableValues = {"Y", "N"}, example = "Y")
    private String displayYn;

    @Schema(description = "정렬(낮을수록 상단). 지정하면 해당 위치부터 뒤 항목 밀림", example = "1")
    private Integer sortOrder;
}
