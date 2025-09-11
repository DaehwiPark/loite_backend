package com.boot.loiteBackend.admin.home.magazinebanner.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminHomeMagazineBannerErrorCode implements ErrorCode {

    NOT_FOUND("HOME_MAGAZINE_BANNER_NOT_FOUND", "해당 배너가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_REQUEST("HOME_MAGAZINE_BANNER_INVALID_REQUEST", "요청 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("HOME_MAGAZINE_BANNER_INTERNAL_ERROR", "배너 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    AdminHomeMagazineBannerErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}