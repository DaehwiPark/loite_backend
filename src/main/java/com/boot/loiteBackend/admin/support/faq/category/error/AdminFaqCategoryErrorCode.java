package com.boot.loiteBackend.admin.support.faq.category.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminFaqCategoryErrorCode implements ErrorCode {

    NOT_FOUND("ADMIN_FAQ_CATEGORY_404", "해당 FAQ 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_NAME("ADMIN_FAQ_CATEGORY_409", "이미 존재하는 카테고리 이름입니다.", HttpStatus.CONFLICT),
    DELETE_FAILED("ADMIN_FAQ_CATEGORY_400", "삭제할 수 없는 카테고리입니다. 관련 FAQ가 존재할 수 있습니다.", HttpStatus.BAD_REQUEST),
    UPDATE_FAILED("ADMIN_FAQ_CATEGORY_400", "카테고리 수정에 실패했습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
