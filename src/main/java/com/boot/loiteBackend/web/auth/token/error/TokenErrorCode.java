package com.boot.loiteBackend.web.auth.token.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    NOT_FOUND("REFRESH_001", "리프레시 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EXPIRED("REFRESH_002", "리프레시 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID("REFRESH_003", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    MISMATCH("REFRESH_004", "사용자 정보와 리프레시 토큰이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
