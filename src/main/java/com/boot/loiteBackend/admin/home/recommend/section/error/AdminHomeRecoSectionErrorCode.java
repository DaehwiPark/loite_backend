package com.boot.loiteBackend.admin.home.recommend.section.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminHomeRecoSectionErrorCode implements ErrorCode {
    INVALID_REQUEST("HRECO_SEC_001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("HRECO_SEC_002", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND("HRECO_SEC_003", "섹션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SAVE_FAILED("HRECO_SEC_004", "저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
