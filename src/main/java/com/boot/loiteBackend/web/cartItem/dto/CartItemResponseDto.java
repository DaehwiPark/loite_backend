package com.boot.loiteBackend.web.cartItem.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private String brandName;
    private String thumbnailUrl;

    private String optionType;
    private String optionValue;
    private Integer optionAdditionalPrice;

    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountedPrice;
    private int discountRate;
    private boolean checked;

    private List<GiftDto> gifts;

    public static class GiftDto {
        private String giftName;
        private String giftImageUrl;
    }

    public String getOptionText() {
        return optionType + " / " + optionValue;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal basePrice = discountedPrice != null ? discountedPrice : unitPrice;
        BigDecimal totalUnit = basePrice.add(BigDecimal.valueOf(optionAdditionalPrice));
        return totalUnit.multiply(BigDecimal.valueOf(quantity));
    }
}


