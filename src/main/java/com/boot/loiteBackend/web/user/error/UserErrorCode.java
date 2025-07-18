package com.boot.loiteBackend.web.user.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    EMAIL_DUPLICATED("USER_409_01", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    PASSWORD_MISMATCH("USER_400_01", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_BIRTHDATE_FORMAT("USER_400_02", "생년월일 형식이 잘못되었습니다. (예: 1990-01-01)", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER_404_01", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    WITHDRAWN_USER("USER_403_01", "탈퇴한 사용자입니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_ACCESS("USER_401_02", "인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS("USER_403_02", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
