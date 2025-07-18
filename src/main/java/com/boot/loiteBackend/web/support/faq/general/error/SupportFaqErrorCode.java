package com.boot.loiteBackend.web.support.faq.general.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SupportFaqErrorCode implements ErrorCode {

    INVALID_PARAMETER("E001", "유효하지 않은 파라미터입니다.", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("E002", "해당 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("E999", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
