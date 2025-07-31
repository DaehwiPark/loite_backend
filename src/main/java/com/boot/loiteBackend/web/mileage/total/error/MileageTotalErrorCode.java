package com.boot.loiteBackend.web.mileage.total.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MileageTotalErrorCode implements ErrorCode {

    NOT_FOUND_MILEAGE_TOTAL("MILEAGE_TOTAL_404", "사용자의 마일리지 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}