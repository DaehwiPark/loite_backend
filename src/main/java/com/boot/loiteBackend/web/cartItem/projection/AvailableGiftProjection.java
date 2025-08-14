package com.boot.loiteBackend.web.cartItem.projection;

public interface AvailableGiftProjection {
    Long getProductGiftId();
    String getGiftName();
    String getGiftImageUrl();
    Integer getGiftStock();
    String getProductName();
    String getOptionValue();
    String getOptionColorCode();
    Integer getQuantity();

    String getGiftSoldOutYn();
}
