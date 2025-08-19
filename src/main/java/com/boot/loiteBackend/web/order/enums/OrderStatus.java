package com.boot.loiteBackend.web.order.enums;

public enum OrderStatus {
    CREATED,              // 주문 생성됨 (결제 전)
    PAID,                 // 결제 완료
    PAYMENT_FAILED,       // 결제 실패
    CANCELLED,            // 주문 취소 (결제 취소 포함)

    RETURN_REQUESTED,     // 반품 요청
    RETURNED,             // 반품 완료
    EXCHANGE_REQUESTED,   // 교환 요청
    EXCHANGED             // 교환 완료
}
