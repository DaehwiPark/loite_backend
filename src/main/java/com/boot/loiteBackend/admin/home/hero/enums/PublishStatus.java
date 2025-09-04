package com.boot.loiteBackend.admin.home.hero.enums;

import lombok.Getter;

@Getter
public enum PublishStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    ARCHIVED("ARCHIVED");

    private final String code;

    PublishStatus(String code) {
        this.code = code;
    }

    public static PublishStatus from(String code) {
        for (PublishStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown publishStatus: " + code);
    }
}
