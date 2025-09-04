package com.boot.loiteBackend.web.payment.enums;

public enum PaymentStatus {
    READY,                  // 결제 준비됨
    PAID,                   // 결제 완료
    CANCELLED,              // 결제 취소
    PARTIAL_CANCELED,       // 부분 취소
    FAILED,                 // 결제 실패
    PAY_PENDING,            // 해외결제 등 승인 대기
    VIRTUAL_ACCOUNT_ISSUED  // 가상계좌 발급 완료
}
