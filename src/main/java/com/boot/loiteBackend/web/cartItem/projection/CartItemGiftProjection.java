package com.boot.loiteBackend.web.cartItem.projection;

public interface CartItemGiftProjection {
    Long getCartItemId();
    Long getProductGiftId();
    String getGiftName();
    String getGiftImageUrl();
    Integer getQuantity();
}
