package com.boot.loiteBackend.admin.product.gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminGiftUpdateRequestDto {
    @Schema(description = "사은풍명", example = "포켓몬빵")
    private String giftName;

    @Schema(description = "사은품 대표이미지", example = "url")
    private String giftImageUrl;

    @Schema(description = "사은품 재고", example = "50")
    private Integer giftStock;

    @Schema(description = "사용 여부", example = "Y, N")
    private String activeYn;
}
