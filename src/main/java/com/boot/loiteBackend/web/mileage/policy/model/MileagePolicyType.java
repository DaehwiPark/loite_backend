package com.boot.loiteBackend.web.mileage.policy.model;

import lombok.Getter;

@Getter
public enum MileagePolicyType {

    FIXED("정액"),
    PERCENTAGE("비율");

    private final String description;

    MileagePolicyType(String description) {
        this.description = description;
    }
}