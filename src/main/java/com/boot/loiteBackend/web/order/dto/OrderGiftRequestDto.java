package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderGiftRequestDto {

    @Schema(description = "사은품 ID", example = "1")
    private Long giftId;

    @Schema(description = "수량", example = "1")
    private int quantity;
}
