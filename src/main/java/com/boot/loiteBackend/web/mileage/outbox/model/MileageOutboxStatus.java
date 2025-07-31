package com.boot.loiteBackend.web.mileage.outbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MileageOutboxStatus {

    PENDING("대기"),
    SUCCESS("성공"),
    FAILED("실패");

    private final String label;
}