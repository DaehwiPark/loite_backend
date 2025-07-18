package com.boot.loiteBackend.admin.support.counsel.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminCounselErrorCode implements ErrorCode {

    NOT_FOUND("ADMIN_COUNSEL_404", "해당 문의 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_ANSWERED("ADMIN_COUNSEL_409", "이미 답변이 등록된 문의입니다.", HttpStatus.CONFLICT),
    REPLY_CONTENT_EMPTY("ADMIN_COUNSEL_400", "답변 내용은 비워둘 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
