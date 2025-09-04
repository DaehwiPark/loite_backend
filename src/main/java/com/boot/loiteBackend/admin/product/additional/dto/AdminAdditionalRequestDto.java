package com.boot.loiteBackend.admin.product.additional.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAdditionalRequestDto {

    @Schema(description = "추가구성품 이름", example = "녹차")
    private String additionalName;

    @Schema(description = "추가구성품 재고 수량", example = "100")
    private Integer additionalStock;

    @Schema(description = "추가구성품 가격", example = "15000")
    private BigDecimal additionalPrice;

    @Schema(description = "추가구성품 이미지", example = "example")
    private String additionalImageUrl;

    @Schema(description = "사용 여부 (Y/N)", example = "Y")
    private Boolean activeYn;
}

