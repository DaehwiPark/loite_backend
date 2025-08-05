package com.boot.loiteBackend.web.support.resource.general.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResourceErrorCode implements ErrorCode {

    DB_RESOURCE_NOT_FOUND("RESOURCE_404_1", "요청한 자료 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND("RESOURCE_404_2", "자료 정보는 존재하지만, 실제 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("RESOURCE_500_1", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("RESOURCE_500_2", "기존 파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}