package com.boot.loiteBackend.admin.product.gift.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminGiftRequestDto {
    private Long giftId;
    private String giftName;
    private String giftImageUrl;
    private Integer giftStock;
    private String activeYn;
}
