package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Schema(description = "기존 수량 표시", example = "2")
    private Integer quantity;

    @Schema(description = "사은품 품절 여부", example = "true,false")
    private boolean giftSoldOutYn;
}
