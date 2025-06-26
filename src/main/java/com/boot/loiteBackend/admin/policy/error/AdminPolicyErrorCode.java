package com.boot.loiteBackend.admin.policy.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminPolicyErrorCode implements ErrorCode {

    NOT_FOUND("POLICY_001", "정책 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SAVE_FAILED("POLICY_002", "정책 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
