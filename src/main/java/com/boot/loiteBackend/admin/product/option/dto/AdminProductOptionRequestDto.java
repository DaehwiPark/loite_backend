package com.boot.loiteBackend.admin.product.option.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductOptionRequestDto {
    private Long optionId;
    private Long productId;
    private String optionType;
    private String optionValue;
    private int optionAdditionalPrice;
    private int optionStock;
    private String optionStyleType;
    private String activeYn;
    private int optionSortOrder;
}
