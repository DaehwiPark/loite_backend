package com.boot.loiteBackend.web.user.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    // 회원가입/로그인 관련
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 사용자 조회
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    WITHDRAWN_USER(HttpStatus.FORBIDDEN, "탈퇴한 사용자입니다."),

    // 인증/인가 관련
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;
}
