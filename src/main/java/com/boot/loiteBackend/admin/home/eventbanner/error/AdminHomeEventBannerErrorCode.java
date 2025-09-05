package com.boot.loiteBackend.admin.home.eventbanner.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminHomeEventBannerErrorCode implements ErrorCode {

    INVALID_REQUEST("EVB_001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("EVB_002", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_FILE("EVB_003", "잘못된 파일입니다.", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED("EVB_004", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SAVE_FAILED("EVB_005", "배너 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("EVB_006", "배너를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
