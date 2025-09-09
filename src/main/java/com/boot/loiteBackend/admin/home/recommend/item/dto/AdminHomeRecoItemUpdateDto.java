package com.boot.loiteBackend.admin.home.recommend.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeRecoItemUpdateDto", description = "추천 아이템 수정 요청 DTO")
public class AdminHomeRecoItemUpdateDto {

    @Schema(description = "아이템 ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;

    @Schema(description = "상품 ID (선택)", example = "200023")
    private Long productId;

    @Schema(description = "노출 슬롯(1~10)", example = "4", minimum = "1", maximum = "10")
    @Min(1) @Max(10)
    private Integer slotNo;

    @Schema(description = "노출 여부", allowableValues = {"Y","N"}, example = "N")
    @Pattern(regexp = "Y|N")
    private String displayYn;

}
