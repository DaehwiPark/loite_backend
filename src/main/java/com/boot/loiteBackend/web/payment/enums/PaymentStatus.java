package com.boot.loiteBackend.web.payment.enums;

public enum PaymentStatus {
    READY,             // 결제 준비됨
    PAID,              // 결제 완료
    CANCELED,         // 결제 취소
    PARTIAL_CANCELED, // 부분 취소
    FAILED,            // 결제 실패
    PENDING            // 해외결제 등 승인 대기
}
