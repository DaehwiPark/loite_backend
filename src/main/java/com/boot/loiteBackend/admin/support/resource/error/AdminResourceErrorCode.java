package com.boot.loiteBackend.admin.support.resource.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AdminResourceErrorCode implements ErrorCode {

    NOT_FOUND("ADMIN_RESOURCE_404", "해당 자료를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("ADMIN_RESOURCE_500_1", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("ADMIN_RESOURCE_500_2", "기존 파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE("ADMIN_RESOURCE_400_1", "잘못된 파일입니다.", HttpStatus.BAD_REQUEST),
    SAVE_FAILED("ADMIN_RESOURCE_500_3", "자료 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UPDATE_FAILED("ADMIN_RESOURCE_500_4", "자료 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_FAILED("ADMIN_RESOURCE_500_5", "자료 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PRODUCT("ADMIN_RESOURCE_400_2", "유효하지 않은 상품입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
