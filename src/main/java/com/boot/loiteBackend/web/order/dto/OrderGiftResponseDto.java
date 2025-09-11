package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderGiftResponseDto {

    @Schema(description = "사은품 ID", example = "501")
    private Long giftId;

    @Schema(description = "사은품명", example = "마우스 패드")
    private String giftName;

    @Schema(description = "사은품명", example = "마우스 패드")
    private String giftImageUrl;

    @Schema(description = "사은품 수량", example = "1")
    private int quantity;
}

