package com.boot.loiteBackend.admin.product.gift.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminGiftResponseDto {
    private Long giftId;
    private String giftName;
    private Integer giftStock;
    private String activeYn;
    private String deleteYn;
}