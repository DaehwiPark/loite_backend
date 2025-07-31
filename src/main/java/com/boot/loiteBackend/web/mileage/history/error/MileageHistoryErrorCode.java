package com.boot.loiteBackend.web.mileage.history.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MileageHistoryErrorCode implements ErrorCode {

    NOT_FOUND("MILEAGE_HISTORY_001", "마일리지 이력이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_TYPE("MILEAGE_HISTORY_002", "잘못된 마일리지 타입입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}