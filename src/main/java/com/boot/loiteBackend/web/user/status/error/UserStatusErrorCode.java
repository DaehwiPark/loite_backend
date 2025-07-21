package com.boot.loiteBackend.web.user.status.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserStatusErrorCode implements ErrorCode {

    STATUS_NOT_FOUND("USER_404_01", "상태 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
    }