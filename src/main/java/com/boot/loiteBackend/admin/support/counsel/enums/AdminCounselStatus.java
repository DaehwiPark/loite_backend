package com.boot.loiteBackend.admin.support.counsel.enums;

import lombok.Getter;

@Getter
public enum AdminCounselStatus {
    WAITING("대기"),
    COMPLETE("완료");

    private final String label;

    AdminCounselStatus(String label) {
        this.label = label;
    }

    public static AdminCounselStatus from(String value) {
        for (AdminCounselStatus status : values()) {
            if (status.name().equalsIgnoreCase(value) || status.label.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 상태입니다: " + value);
    }
}
