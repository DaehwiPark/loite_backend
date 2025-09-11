package com.boot.loiteBackend.web.home.mgazineBanner.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HomeMagazineBannerErrorCode implements ErrorCode {
    NOT_FOUND("HOME_MAGAZINE_BANNER_NOT_FOUND", "노출 가능한 매거진 배너가 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    HomeMagazineBannerErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}