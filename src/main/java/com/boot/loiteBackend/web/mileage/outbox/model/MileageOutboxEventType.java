package com.boot.loiteBackend.web.mileage.outbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MileageOutboxEventType {

    SIGN_UP_BONUS("회원가입 적립"),
    ORDER_REWARD("주문 적립"),
    REVIEW_REWARD("리뷰 적립");

    private final String label;
}