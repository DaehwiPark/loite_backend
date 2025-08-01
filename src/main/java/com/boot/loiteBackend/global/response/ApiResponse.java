package com.boot.loiteBackend.global.response;

import com.boot.loiteBackend.global.error.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Schema(description = "공통 API 응답 포맷")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    @Schema(description = "메시지 또는 설명", example = "요청이 정상 처리되었습니다.")
    private String message;

    @Schema(description = "에러 코드 (실패 시에만 존재)", example = "KAKAO_200")
    private String code;

    @Schema(description = "응답 데이터 (성공 시)", nullable = true)
    private T data;

    @Schema(description = "추가 정보 (선택적)", example = "{\"email\": \"user@loite.com\"}")
    private Map<String, Object> extra;

    // 성공 응답 헬퍼
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> ok(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> ok(T data, String message, Map<String, Object> extra) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .extra(extra)
                .build();
    }

    // 실패 응답 헬퍼
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, Map<String, Object> extra) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .extra(extra)
                .build();
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String customMessage) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(customMessage)
                .code(errorCode.getCode())
                .build();
    }
}
