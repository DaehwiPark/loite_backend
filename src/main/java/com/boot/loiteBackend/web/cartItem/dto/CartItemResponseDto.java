package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {

    @Schema(description = "장바구니 항목 ID", example = "2001")
    private Long cartItemId;

    @Schema(description = "상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "상품명", example = "프리미엄 원목 테이블")
    private String productName;

    @Schema(description = "브랜드명", example = "로이테가구")
    private String brandName;

    @Schema(description = "대표 썸네일 이미지 URL", example = "https://cdn.loite.com/images/product/thumb-wood-table.jpg")
    private String thumbnailUrl;

    @Schema(description = "선택한 옵션 타입 (예: 색상)", example = "색상")
    private String optionType;

    @Schema(description = "선택한 옵션 값", example = "내추럴 우드")
    private String optionValue;

    @Schema(description = "옵션 추가 금액", example = "5000")
    private Integer optionAdditionalPrice;

    @Schema(description = "선택한 사은품명", example = "코튼 러그")
    private String giftName;

    @Schema(description = "사은품 이미지 URL", example = "https://cdn.loite.com/images/gift/rug.jpg")
    private String giftImageUrl;

    @Schema(description = "주문 수량", example = "2")
    private int quantity;

    @Schema(description = "정가 (단가)", example = "299000.00")
    private BigDecimal unitPrice;

    @Schema(description = "할인가 (단가)", example = "269100.00")
    private BigDecimal discountedPrice;

    @Schema(description = "할인율 (%)", example = "10")
    private int discountRate;

    @Schema(description = "체크 여부 (UI 선택용)", example = "true")
    private boolean checked;

    public BigDecimal getTotalPrice() {
        BigDecimal basePrice = discountedPrice != null ? discountedPrice : unitPrice;
        BigDecimal totalUnit = basePrice.add(BigDecimal.valueOf(optionAdditionalPrice));
        return totalUnit.multiply(BigDecimal.valueOf(quantity));
    }
}


