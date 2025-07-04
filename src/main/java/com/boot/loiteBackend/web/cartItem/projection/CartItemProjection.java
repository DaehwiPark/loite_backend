package com.boot.loiteBackend.web.cartItem.projection;

public interface CartItemProjection {

    Long getCartItemId();      // 장바구니 항목 ID
    Long getProductId();       // 상품 ID
    String getProductName();   // 상품 이름
    String getBrandName();     // 브랜드명
    String getThumbnailUrl();  // 대표 이미지 URL
    String getOptionText();    // 옵션 텍스트
    Integer getQuantity();     // 수량
    Integer getUnitPrice();    // 단가 (할인가 기준)
    Boolean getChecked();      // 체크 여부
}