package com.boot.loiteBackend.web.support.notice.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SupportNoticeErrorCode implements ErrorCode {

    NOT_FOUND("ADMIN_NOTICE_404", "해당 공지사항을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
