package com.boot.loiteBackend.web.user.address.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserAddressErrorCode implements ErrorCode {

    // 4xx
    ADDRESS_NOT_FOUND("ADDR_404", "주소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_ADDRESS("ADDR_403", "해당 주소에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_REQUEST("ADDR_400", "요청 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    DEFAULT_ADDRESS_NOT_FOUND("ADDR_DEFAULT_404", "기본 배송지가 없습니다.", HttpStatus.NOT_FOUND), // ★ 추가

    INTERNAL_ERROR("ADDR_500", "배송지 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code;
    private final String message;
    private final HttpStatus status;
}
