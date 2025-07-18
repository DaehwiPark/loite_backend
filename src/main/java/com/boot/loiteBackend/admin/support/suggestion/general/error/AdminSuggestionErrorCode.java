package com.boot.loiteBackend.admin.support.suggestion.general.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AdminSuggestionErrorCode implements ErrorCode {

    NOT_FOUND("ADMIN_SUGGESTION_404", "해당 제안을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DELETE_FAILED("ADMIN_SUGGESTION_400_1", "제안 삭제에 실패했습니다.", HttpStatus.BAD_REQUEST),
    UPDATE_FAILED("ADMIN_SUGGESTION_400_2", "제안 수정에 실패했습니다.", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND("ADMIN_SUGGESTION_404_1", "첨부파일 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_NOT_EXIST_ON_DISK("ADMIN_SUGGESTION_500_1", "서버에 첨부파일이 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
