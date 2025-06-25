package com.boot.loiteBackend.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public class ErrorResponseUtil {

    /**
     * ErrorResponse 응답을 구성하여 ResponseEntity로 반환
     */
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    /**
     * 예외 객체로부터 500번 에러를 생성
     */
    public static ResponseEntity<ErrorResponse> toInternalServerError(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                500,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(500).body(errorResponse);
    }
}
