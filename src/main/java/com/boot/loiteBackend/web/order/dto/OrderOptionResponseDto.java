package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderOptionResponseDto {

    @Schema(description = "옵션 ID", example = "301")
    private Long optionId;

    @Schema(description = "옵션 값", example = "화이트 / 500L")
    private String optionValue;

    @Schema(description = "옵션 추가금", example = "5000")
    private BigDecimal additionalPrice;

    @Schema(description = "옵션 수량", example = "1")
    private int quantity;
}
