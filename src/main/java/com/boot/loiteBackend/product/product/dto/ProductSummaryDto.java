package com.boot.loiteBackend.product.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "제품 요약 정보 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSummaryDto {

    @Schema(description = "제품 ID", example = "1001")
    private Long productId;

    @Schema(description = "제품명", example = "Air Conditioner")
    private String productName;

    @Schema(description = "모델명", example = "AC-1234X")
    private String productModelName;
}
