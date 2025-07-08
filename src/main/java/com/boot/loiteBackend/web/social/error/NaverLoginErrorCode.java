package com.boot.loiteBackend.web.social.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NaverLoginErrorCode implements ErrorCode {

    UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "NAVER-001", "지원하지 않는 로그인 제공자입니다."),
    ALREADY_REGISTERED(HttpStatus.CONFLICT, "NAVER-002", "이미 다른 방법으로 가입된 사용자입니다."),
    FAILED_TO_GET_TOKEN(HttpStatus.UNAUTHORIZED, "NAVER-003", "네이버 Access Token 발급에 실패했습니다."),
    FAILED_TO_GET_USER(HttpStatus.UNAUTHORIZED, "NAVER-004", "네이버 사용자 정보 조회에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
