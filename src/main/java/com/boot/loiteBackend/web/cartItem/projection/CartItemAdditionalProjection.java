package com.boot.loiteBackend.web.cartItem.projection;

public interface CartItemAdditionalProjection {
    Long getCartItemId();
    Long getProductAdditionalId();
    String getAdditionalName();
    String getAdditionalImageUrl();
    Integer getAdditionalStock();
    Integer getQuantity();
}
