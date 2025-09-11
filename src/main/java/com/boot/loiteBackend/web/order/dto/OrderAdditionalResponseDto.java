package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAdditionalResponseDto {

    @Schema(description = "추가구성품 ID", example = "401")
    private Long additionalId;

    @Schema(description = "추가구성품명", example = "전용 거치대")
    private String additionalName;

    @Schema(description = "추가구성품 금액", example = "10000")
    private BigDecimal additionalPrice;

    @Schema(description = "추가구성품 수량", example = "1")
    private int quantity;
}

