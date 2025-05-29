package com.boot.loiteMsBack.support.suggestion.error;

import com.boot.loiteMsBack.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SuggestionErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 제안을 찾을 수 없습니다."),
    DELETE_FAILED(HttpStatus.BAD_REQUEST, "제안 삭제에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
