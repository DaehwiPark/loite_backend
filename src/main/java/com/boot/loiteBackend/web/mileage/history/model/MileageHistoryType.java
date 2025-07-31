package com.boot.loiteBackend.web.mileage.history.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum MileageHistoryType {

    EARN("적립"),
    USE("사용");

    private final String label;
}