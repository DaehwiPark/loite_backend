package com.boot.loiteBackend.admin.support.faq.general.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminFaqErrorCode implements ErrorCode {

    NOT_FOUND("ADMIN_FAQ_404", "해당 FAQ 항목을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DELETE_FAILED("ADMIN_FAQ_400", "FAQ 항목 삭제에 실패했습니다.", HttpStatus.BAD_REQUEST),
    UPDATE_FAILED("ADMIN_FAQ_400", "FAQ 항목 수정에 실패했습니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("ADMIN_FAQ_404", "FAQ 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
