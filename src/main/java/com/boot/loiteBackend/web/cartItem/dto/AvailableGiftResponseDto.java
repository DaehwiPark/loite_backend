package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableGiftResponseDto {

    @Schema(description = "연결 상품의 사은품 ID", example = "1")
    private Long productGiftId;

    @Schema(description = "사은품 이름", example = "피카츄빵")
    private String giftName;

    @Schema(description = "사은품 이미지 URL", example = "image")
    private String giftImageUrl;

    @Schema(description = "사은품 재고", example = "50")
    private Integer giftStock;

    @Schema(description = "상품 이름", example = "주전자")
    private String productName;

    @Schema(description = "옵션 이름", example = "화이트")
    private String optionValue;

    @Schema(description = "컬러 코드", example = "#ffffff")
    private String optionColorCode;

}
