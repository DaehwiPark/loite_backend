package com.boot.loiteBackend.admin.terms.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminTermsErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "TERMS_001", "이용약관을 찾을 수 없습니다."),
    SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "TERMS_002", "이용약관 저장에 실패했습니다."),
    DELETE_FAILED(HttpStatus.BAD_REQUEST, "TERMS_003", "존재하지 않는 이용약관입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
