package com.boot.loiteMsBack.support.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "제품 리소스 등록 요청 DTO")
@Getter
@Setter
public class SupportResourceRequestDto {

    @Schema(description = "제품 이름", example = "Air Conditioner", required = true)
    private String resourceProductName;

    @Schema(description = "제품 모델 명", example = "AC-1234X", required = false)
    private String resourceModelName;
}
