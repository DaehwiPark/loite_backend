package com.boot.loiteBackend.web.social.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GoogleLoginErrorCode implements ErrorCode {

    FAILED_TO_GET_TOKEN("GOOGLE_001", "구글 토큰 발급에 실패했습니다.", HttpStatus.BAD_REQUEST),
    FAILED_TO_GET_USER("GOOGLE_002", "구글 사용자 정보를 가져오지 못했습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_REGISTERED_WITH_OTHER_PROVIDER("GOOGLE_409", "다른 로그인 방식으로 가입된 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_REGISTERED("GOOGLE_404", "회원가입이 필요한 사용자입니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
