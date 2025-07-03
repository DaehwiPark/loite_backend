package com.boot.loiteBackend.global.error;

import com.boot.loiteBackend.global.error.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "API 에러 응답 포맷")
public class ErrorResponse {

    @Schema(description = "응답 시각", example = "2025-07-03T12:34:56")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP 상태 코드", example = "404")
    private int status;

    @Schema(description = "에러명", example = "Not Found")
    private String error;

    @Schema(description = "에러 메시지", example = "해당 문의 내역을 찾을 수 없습니다.")
    private String message;

    @Schema(description = "커스텀 에러 코드", example = "ADMIN_COUNSEL_404")
    private String code;

    @Schema(description = "요청 경로", example = "/api/support/counsel/1")
    private String path;

    @Schema(description = "추가 정보", example = "{\"userId\": \"123\"}")
    private Map<String, Object> extra;

    public static ErrorResponse of(int status, String error, String message, String path, String errorCode) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .code(errorCode)
                .build();
    }

    public static ErrorResponse from(CustomException ex, HttpServletRequest request) {
        ErrorCode code = ex.getErrorCode();
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(code.getStatus().value())
                .error(code.getStatus().getReasonPhrase())
                .message(code.getMessage())
                .code(code.getCode())
                .path(request.getRequestURI())
                .build();
    }
}
