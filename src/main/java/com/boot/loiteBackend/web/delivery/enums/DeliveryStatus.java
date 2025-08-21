package com.boot.loiteBackend.web.delivery.enums;

public enum DeliveryStatus {
    READY,        // 배송 준비중 (상품 준비/포장 단계)
    IN_PROGRESS,  // 배송중 (택배사 인계 → 고객에게 이동중)
    DELIVERED,    // 배송 완료

    RETURNING,    // 반품 회수중
    RETURNED,     // 반품 완료

    EXCHANGING,   // 교환 배송중
    EXCHANGED     // 교환 완료
}
