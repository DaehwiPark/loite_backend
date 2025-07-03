package com.boot.loiteBackend.web.user.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    EMAIL_DUPLICATED("USER_001", HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD("USER_002", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    PASSWORD_MISMATCH("USER_003", HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다."),
    INVALID_BIRTHDATE_FORMAT("USER_004", HttpStatus.BAD_REQUEST, "생년월일 형식이 올바르지 않습니다. (예: 1990-01-01)"),
    USER_NOT_FOUND("USER_005", HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    WITHDRAWN_USER("USER_006", HttpStatus.FORBIDDEN, "탈퇴한 사용자입니다."),
    UNAUTHORIZED_ACCESS("USER_007", HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다."),
    FORBIDDEN_ACCESS("USER_008", HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
