package com.boot.loiteBackend.web.auth.login.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LoginErrorCode implements ErrorCode {

    NOT_FOUND("LOGIN_001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("LOGIN_002", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAIL("LOGIN_003", "로그인에 실패했습니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
