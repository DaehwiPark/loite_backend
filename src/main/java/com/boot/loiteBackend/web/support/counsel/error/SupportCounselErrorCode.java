package com.boot.loiteBackend.web.support.counsel.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SupportCounselErrorCode implements ErrorCode {

    NOT_FOUND("SC001", "문의 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCESS_DENIED("SC002", "해당 문의에 접근할 수 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_PASSWORD("SC003", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    ALREADY_DELETED("SC004", "이미 삭제된 문의입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("SC999", "1:1 문의 처리 중 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}