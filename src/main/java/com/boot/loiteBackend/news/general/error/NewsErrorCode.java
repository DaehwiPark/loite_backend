package com.boot.loiteBackend.news.general.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 뉴스 도메인 관련 에러 코드 정의
 */
@Getter
@RequiredArgsConstructor
public enum NewsErrorCode implements ErrorCode {

    INVALID_THUMBNAIL(HttpStatus.BAD_REQUEST, "NEWS_001", "유효하지 않은 썸네일 파일입니다."),
    THUMBNAIL_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NEWS_002", "썸네일 이미지 업로드에 실패했습니다."),
    SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NEWS_003", "뉴스 저장에 실패했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NEWS_004", "해당 뉴스가 존재하지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "NEWS_005", "요청 형식이 잘못되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
