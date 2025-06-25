package com.boot.loiteBackend.support.counsel.enums;

import lombok.Getter;

@Getter
public enum CounselStatus {
    WAITING("대기"),
    COMPLETE("완료");

    private final String label;

    CounselStatus(String label) {
        this.label = label;
    }

    public static CounselStatus from(String value) {
        for (CounselStatus status : values()) {
            if (status.name().equalsIgnoreCase(value) || status.label.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 상태입니다: " + value);
    }
}
