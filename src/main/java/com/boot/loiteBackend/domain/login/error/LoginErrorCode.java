package com.boot.loiteBackend.domain.login.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LoginErrorCode implements ErrorCode {

    NOT_FOUND("LOGIN_001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("LOGIN_002", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAIL("LOGIN_003", "로그인에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("LOGIN_004", "로그인된 사용자만 접근할 수 있습니다.", HttpStatus.UNAUTHORIZED),
    SOCIAL_USER_CANNOT_VERIFY_PASSWORD("LOGIN_005", "비밀번호 인증이 불가능한 소셜 로그인 사용자입니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
