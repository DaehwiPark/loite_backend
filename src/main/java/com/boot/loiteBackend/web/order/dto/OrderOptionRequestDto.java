package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOptionRequestDto {

    @Schema(description = "옵션 ID", example = "1")
    private Long optionId;

    @Schema(description = "수량", example = "1")
    private int quantity;

    /*@Schema(description = "옵션 추가금액", example = "3,000")
    private BigDecimal additionalPrice;*/
}
