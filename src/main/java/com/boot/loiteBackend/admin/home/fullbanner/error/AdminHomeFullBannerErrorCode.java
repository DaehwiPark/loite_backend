package com.boot.loiteBackend.admin.home.fullbanner.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminHomeFullBannerErrorCode implements ErrorCode {
    INVALID_REQUEST("FULL_001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("FULL_002", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_FILE("FULL_003", "잘못된 파일입니다.", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED("FULL_004", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SAVE_FAILED("FULL_005", "저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("FULL_006", "풀 배너를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
