package com.boot.loiteBackend.domain.social.error;

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
    FAILED_TO_REGISTER_USER("KAKAO_004", "카카오 사용자 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_REGISTERED_WITH_OTHER_PROVIDER("KAKAO_409", "다른 로그인 방식으로 가입된 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_REGISTERED("KAKAO_404", "회원가입이 필요한 사용자입니다.", HttpStatus.NOT_FOUND),
    EMAIL_NOT_PROVIDED("KAKAO_400", "카카오에서 이메일을 제공하지 않았습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
