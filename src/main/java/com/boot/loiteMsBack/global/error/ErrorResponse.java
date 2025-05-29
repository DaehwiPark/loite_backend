package com.boot.loiteMsBack.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Schema(description = "API 에러 응답 포맷")
public class ErrorResponse {

    @Schema(description = "응답 시각", example = "2025-05-29T12:34:56")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP 상태 코드", example = "404")
    private int status;

    @Schema(description = "에러명", example = "Not Found")
    private String error;

    @Schema(description = "에러 메시지", example = "해당 문의 내역을 찾을 수 없습니다.")
    private String message;

    @Schema(description = "요청 경로", example = "/api/support/counsel/1")
    private String path;

    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
}
