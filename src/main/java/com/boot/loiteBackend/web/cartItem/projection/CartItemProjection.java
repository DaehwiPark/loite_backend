package com.boot.loiteBackend.web.cartItem.projection;

import java.math.BigDecimal;

public interface CartItemProjection {

    Long getCartItemId();
    Long getProductId();
    String getProductName();
    String getBrandName();
    String getThumbnailUrl();

    String getOptionType();
    String getOptionValue();
    Integer getOptionAdditionalPrice();

    Integer getQuantity();
    BigDecimal getUnitPrice();
    BigDecimal getDiscountedPrice();
    Integer getDiscountRate();

    Integer getChecked();      // 체크 여부
}