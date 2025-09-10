package com.boot.loiteBackend.web.home.recommend.item.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeRecoItemErrorCode implements ErrorCode {

    SECTION_NOT_FOUND("HRI-404-001", "섹션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
