package com.boot.loiteBackend.admin.login.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminLoginErrorCode implements ErrorCode {
    USER_NOT_FOUND("ADMIN_LOGIN_001", "관리자 계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("ADMIN_LOGIN_002", "비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    INACTIVE_STATUS("ADMIN_LOGIN_003", "현재 상태에서는 로그인할 수 없습니다.", HttpStatus.FORBIDDEN),
    SOCIAL_ACCOUNT("ADMIN_LOGIN_004", "소셜 로그인 전용 계정입니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN_ROLE("ADMIN_LOGIN_005", "관리자 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
