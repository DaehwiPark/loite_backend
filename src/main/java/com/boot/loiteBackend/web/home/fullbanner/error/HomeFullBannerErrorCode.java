package com.boot.loiteBackend.web.home.fullbanner.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeFullBannerErrorCode implements ErrorCode {

    ACTIVE_NOT_FOUND("FULLBANNER_001", "노출 가능한 대표 풀 배너가 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
