package com.boot.loiteBackend.web.home.topbar.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeTopBarErrorCode implements ErrorCode {

    ACTIVE_NOT_FOUND("TOPBAR_404", "활성화된 TopBar가 없습니다.", HttpStatus.NOT_FOUND),
    INTERNAL_ERROR("TOPBAR_500", "일시적인 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

}