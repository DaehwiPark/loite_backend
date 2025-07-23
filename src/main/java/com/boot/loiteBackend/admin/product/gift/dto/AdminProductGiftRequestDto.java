package com.boot.loiteBackend.admin.product.gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AdminProductGiftRequestDto {
    @Schema(description = "사은품 ID", example = "1, 2, 3 ...")
    private Long giftId;
}
