package com.boot.loiteBackend.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 에러 응답 객체를 구성해주는 유틸리티 클래스
 */
public class ErrorResponseUtil {

    /**
     * ErrorCode 기반으로 에러 응답 구성
     *
     * @param errorCode ErrorCode 구현체
     * @param request   요청 정보
     * @return 에러 응답 ResponseEntity
     */
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.getMessage(),
                request.getRequestURI(),
                errorCode.getCode()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    /**
     * 알 수 없는 서버 오류(500) 발생 시 응답 생성
     *
     * @param ex      예외 객체
     * @param request 요청 정보
     * @return 에러 응답 ResponseEntity
     */
    public static ResponseEntity<ErrorResponse> toInternalServerError(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
