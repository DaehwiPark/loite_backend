package com.boot.loiteBackend.web.cartItem.projection;

import java.math.BigDecimal;

public interface CartItemAdditionalProjection {
    Long getCartItemId();
    Long getProductAdditionalId();
    String getAdditionalName();
    String getAdditionalImageUrl();
    Integer getAdditionalStock();
    BigDecimal getAdditionalPrice();
    Integer getQuantity();
}
