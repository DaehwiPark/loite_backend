package com.boot.loiteBackend.web.social.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SocialErrorCode implements ErrorCode {

    // 요청 오류
    INVALID_PROVIDER("SOCIAL_001", "지원하지 않는 소셜 로그인 제공자입니다.", HttpStatus.BAD_REQUEST),
    INVALID_SOCIAL_TOKEN("SOCIAL_015", "소셜 인증 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),

    // 인증 실패
    SOCIAL_VERIFICATION_FAILED("SOCIAL_013", "소셜 인증 정보가 연동된 계정과 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    // 사용자 상태
    USER_NOT_REGISTERED("SOCIAL_006", "회원가입이 필요한 사용자입니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("SOCIAL_014", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_FOR_LINKING("SOCIAL_009", "기존 계정을 찾을 수 없습니다. 로그인 후 연동을 시도해주세요.", HttpStatus.NOT_FOUND),

    // 계정 연동 상태
    ALREADY_REGISTERED_WITH_OTHER_PROVIDER("SOCIAL_005", "이미 다른 소셜 제공자로 가입된 이메일입니다.", HttpStatus.CONFLICT),
    ALREADY_LINKED("SOCIAL_010", "해당 소셜 계정은 이미 연동되어 있습니다.", HttpStatus.CONFLICT),
    SOCIAL_NOT_LINKED("SOCIAL_011", "해당 소셜 계정은 연동되어 있지 않습니다.", HttpStatus.BAD_REQUEST),

    // 내부 처리 오류
    FAILED_TO_GET_AUTH_URL("SOCIAL_002", "소셜 로그인 인증 URL 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_GET_TOKEN("SOCIAL_003", "소셜 액세스 토큰 발급에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_GET_USER("SOCIAL_004", "소셜 사용자 정보 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_REGISTER_USER("SOCIAL_007", "소셜 사용자 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNLINK_FAILED("SOCIAL_012", "소셜 연동 해제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}