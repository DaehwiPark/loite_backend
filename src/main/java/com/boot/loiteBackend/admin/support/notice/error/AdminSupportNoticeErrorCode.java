package com.boot.loiteBackend.admin.support.notice.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AdminSupportNoticeErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공지사항을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
