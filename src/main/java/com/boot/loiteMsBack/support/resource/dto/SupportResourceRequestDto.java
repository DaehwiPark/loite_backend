package com.boot.loiteMsBack.support.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "제품 리소스 등록 요청 DTO")
@Data
public class SupportResourceRequestDto {

    @Schema(description = "제품 이름", example = "Air Conditioner", required = true)
    private String resourceProductName;

    @Schema(description = "제품 모델 명", example = "AC-1234X", required = false)
    private String resourceModelName;

    @Schema(description = "노출 여부", example = " Y", required = false)
    private String displayYn;
}
