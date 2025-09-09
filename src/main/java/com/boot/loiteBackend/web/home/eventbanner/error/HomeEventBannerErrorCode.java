package com.boot.loiteBackend.web.home.eventbanner.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeEventBannerErrorCode implements ErrorCode {

    INVALID_REQUEST("HEB_001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("HEB_002", "배너를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SAVE_FAILED("HEB_003", "배너 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
