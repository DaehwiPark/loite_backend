package com.boot.loiteBackend.admin.home.recommend.section.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name="AdminHomeRecoSectionUpdateDto", description="홈 추천 섹션 수정 요청 DTO")
public class AdminHomeRecoSectionUpdateDto {

    @Schema(description="섹션 ID", example="10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;

    @Schema(description="좌측 박스 타이틀", example="혼자서도 충분히")
    private String title;

    @Schema(description="이동 URL", example="/collections/solo")
    private String linkUrl;

    @Schema(description="링크 타겟", allowableValues={"_self","_blank"}, example="_blank")
    private String linkTarget;

    @Schema(description="노출 여부", allowableValues={"Y","N"}, example="N")
    private String displayYn;

    @Schema(description="정렬(낮을수록 상단). 값 변경 시 자동 재정렬", example="1")
    private Integer sortOrder;
}
