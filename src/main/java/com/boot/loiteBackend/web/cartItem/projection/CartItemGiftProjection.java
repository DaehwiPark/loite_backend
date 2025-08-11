package com.boot.loiteBackend.web.cartItem.projection;

public interface CartItemGiftProjection {
    Long getCartItemId();
    Long getProductGiftId();
    String getGiftName();
    String getGiftImageUrl();
    Integer getGiftStock();
    Integer getQuantity();

    /*Long getProductId();
    String getProductName();

    Long getOptionId();
    String getOptionValue();
    String getOptionColorCode();*/
}
