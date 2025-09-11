package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAdditionalRequestDto {

    @Schema(description = "추가구셩품 ID", example = "1")
    private Long additionalId;

    @Schema(description = "수량", example = "1")
    private int quantity;

    /*@Schema(description = "추가구성품 추가금액", example = "10,000")
    private BigDecimal additionalPrice;*/
}
