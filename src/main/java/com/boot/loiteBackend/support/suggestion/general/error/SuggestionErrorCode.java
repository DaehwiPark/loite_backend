package com.boot.loiteBackend.support.suggestion.general.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SuggestionErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 제안을 찾을 수 없습니다."),
    DELETE_FAILED(HttpStatus.BAD_REQUEST, "제안 삭제에 실패했습니다."),
    UPDATE_FAILED(HttpStatus.BAD_REQUEST, "제안 수정에 실패했습니다."),

    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "첨부파일 정보를 찾을 수 없습니다."),
    FILE_NOT_EXIST_ON_DISK(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 첨부파일이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
