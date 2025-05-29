package com.boot.loiteMsBack.support.counsel.error;

import com.boot.loiteMsBack.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CounselErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 문의 내역을 찾을 수 없습니다."),
    ALREADY_ANSWERED(HttpStatus.CONFLICT, "이미 답변이 등록된 문의입니다.");

    private final HttpStatus status;
    private final String message;
}
