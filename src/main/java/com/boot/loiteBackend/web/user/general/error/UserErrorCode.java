package com.boot.loiteBackend.web.user.general.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    EMAIL_DUPLICATED("USER_409_01", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    PASSWORD_MISMATCH("USER_400_01", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_BIRTHDATE_FORMAT("USER_400_02", "생년월일 형식이 잘못되었습니다. (예: 1990-01-01)", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER_404_01", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    WITHDRAWN_USER("USER_403_01", "탈퇴한 사용자입니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_ACCESS("USER_401_02", "인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS("USER_403_02", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    SOCIAL_LINK_EXISTS("USER_400_03", "소셜 계정 연동이 남아 있습니다. 연동 해제 후 탈퇴가 가능합니다.", HttpStatus.BAD_REQUEST),
    SOCIAL_UNLINK_FAILED("USER_500_01", "소셜 연동 해제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}