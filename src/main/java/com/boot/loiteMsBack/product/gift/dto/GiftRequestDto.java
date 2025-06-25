package com.boot.loiteMsBack.product.gift.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GiftRequestDto {
    private Long giftId;
    private String giftName;
    private Integer giftStock;
    private String activeYn;
}
