package com.boot.loiteBackend.admin.support.resource.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AdminResourceErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 자료를 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "기존 파일 삭제에 실패했습니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "잘못된 파일입니다."),
    SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "자료 저장에 실패했습니다."),
    UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "자료 수정에 실패했습니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "자료 삭제에 실패했습니다."),
    INVALID_PRODUCT(HttpStatus.BAD_REQUEST, "유효하지 않은 상품입니다.");

    private final HttpStatus status;
    private final String message;
}
