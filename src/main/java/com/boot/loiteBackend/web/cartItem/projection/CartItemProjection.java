package com.boot.loiteBackend.web.cartItem.projection;

import java.math.BigDecimal;

public interface CartItemProjection {

    Long getCartItemId();
    Long getProductId();
    String getProductName();
    String getBrandName();
    String getThumbnailUrl();
    Integer getProductStock();

    String getOptionType();
    String getOptionValue();
    Integer getOptionAdditionalPrice();
    Integer getOptionStock();

    String getGiftName();
    String getGiftImageUrl();

    Integer getQuantity();
    BigDecimal getUnitPrice();
    BigDecimal getDiscountedPrice();
    Integer getDiscountRate();

    Integer getChecked();      // 체크 여부
}