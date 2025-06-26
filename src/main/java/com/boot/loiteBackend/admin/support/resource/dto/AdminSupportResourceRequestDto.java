package com.boot.loiteBackend.admin.support.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "제품 리소스 등록 요청 DTO")
@Data
public class AdminSupportResourceRequestDto {

    @Schema(description = "상품 ID", example = "1001", required = true)
    private Long productId;

    @Schema(description = "노출 여부", example = "Y", required = false)
    private String displayYn;
}
