package com.boot.loiteBackend.admin.home.topbar.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminHomeTopBarErrorCode implements ErrorCode {

    TOPBAR_NOT_FOUND("AHTB_404", "탑바를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_REQUEST("AHTB_400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("AHTB_401", "인증되지 않았습니다.", HttpStatus.UNAUTHORIZED),
    DUPLICATE_DEFAULT("AHTB_409", "이미 다른 탑바가 기본으로 설정되어 있습니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}