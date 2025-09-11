package com.boot.loiteBackend.web.home.best.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeBestItemErrorCode implements ErrorCode {

    NOT_FOUND("HOME_BEST_NOT_FOUND", "인기 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
