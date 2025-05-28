package com.boot.loiteMsBack.support.counsel.enums;

import lombok.Getter;

@Getter
public enum CounselStatus {
    대기("대기"),
    처리중("처리중"),
    완료("완료");

    private final String label;

    CounselStatus(String label) {
        this.label = label;
    }

    public static CounselStatus from(String value) {
        for (CounselStatus status : values()) {
            if (status.name().equalsIgnoreCase(value) || status.getLabel().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 상태입니다: " + value);
    }
}
