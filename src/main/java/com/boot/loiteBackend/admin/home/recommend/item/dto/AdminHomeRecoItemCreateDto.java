package com.boot.loiteBackend.admin.home.recommend.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeRecoItemCreateDto", description = "추천 아이템 생성 요청 DTO")
public class AdminHomeRecoItemCreateDto {

    @Schema(description = "추천 섹션 ID (FK)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long sectionId;

    @Schema(description = "상품 ID (FK)", example = "200023")
    private Long productId;

    @Schema(description = "노출 슬롯(1~10)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "10")
    @NotNull
    @Min(1)
    @Max(10)
    private Integer slotNo;

    @Schema(description = "노출 여부", allowableValues = {"Y", "N"}, example = "Y")
    @Pattern(regexp = "Y|N")
    private String displayYn;

}
