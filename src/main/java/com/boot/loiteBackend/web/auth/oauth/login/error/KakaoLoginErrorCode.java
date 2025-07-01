package com.boot.loiteBackend.web.auth.oauth.login.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KakaoLoginErrorCode implements ErrorCode {
    FAILED_TO_GET_AUTH_URL("KAKAO_001", "카카오 로그인 URL 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_GET_TOKEN("KAKAO_002", "카카오 토큰 발급에 실패했습니다.", HttpStatus.BAD_REQUEST),
    FAILED_TO_GET_USER("KAKAO_003", "카카오 사용자 정보를 가져오지 못했습니다.", HttpStatus.BAD_REQUEST),
    FAILED_TO_REGISTER_USER("KAKAO_004", "카카오 사용자 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
