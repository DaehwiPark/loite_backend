package com.boot.loiteBackend.web.cartItem.projection;

import java.math.BigDecimal;

public interface CartItemOptionProjection {
    Long getCartItemId();
    Long getOptionId();
    String getOptionValue();
    String getOptionType();
    Integer getOptionStock();
    Integer getQuantity();
    BigDecimal getOptionAdditionalPrice();
}
