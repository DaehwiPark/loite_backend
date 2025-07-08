package com.boot.loiteBackend.web.social.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SocialErrorCode implements ErrorCode {

    INVALID_PROVIDER("SOCIAL_001", "지원하지 않는 소셜 로그인 제공자입니다.", HttpStatus.BAD_REQUEST),
    FAILED_TO_GET_AUTH_URL("SOCIAL_002", "소셜 로그인 URL 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_GET_TOKEN("SOCIAL_003", "소셜 액세스 토큰 발급에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_GET_USER("SOCIAL_004", "소셜 사용자 정보 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_REGISTERED_WITH_OTHER_PROVIDER("SOCIAL_005", "이미 다른 소셜 제공자로 가입된 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_REGISTERED("SOCIAL_006", "회원가입이 필요한 사용자입니다.", HttpStatus.NOT_FOUND),
    FAILED_TO_REGISTER_USER("SOCIAL_007", "소셜 사용자 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNSUPPORTED_PROVIDER("SOCIAL_008", "지원하지 않는 소셜 로그인 타입입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND_FOR_LINKING("SOCIAL_009", "기존 계정을 찾을 수 없습니다. 로그인 후 연동을 시도해주세요.", HttpStatus.NOT_FOUND),
    ALREADY_LINKED("SOCIAL_010", "해당 소셜 계정은 이미 연동되어 있습니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
