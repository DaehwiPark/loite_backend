package com.boot.loiteBackend.admin.support.faq.category.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminFaqCategoryErrorCode implements ErrorCode {

    // ===== [공통] =====
    NOT_FOUND("ADMIN_FAQ_CATEGORY_404", "해당 FAQ 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_NAME("ADMIN_FAQ_CATEGORY_409", "이미 존재하는 카테고리 이름입니다.", HttpStatus.CONFLICT),
    DELETE_FAILED("ADMIN_FAQ_CATEGORY_400", "삭제할 수 없는 카테고리입니다. 관련 FAQ가 존재할 수 있습니다.", HttpStatus.BAD_REQUEST),
    UPDATE_FAILED("ADMIN_FAQ_CATEGORY_400", "카테고리 수정에 실패했습니다.", HttpStatus.BAD_REQUEST),

    // ===== [대분류 관련] =====
    MAJOR_CATEGORY_NOT_FOUND("ADMIN_FAQ_MAJOR_CATEGORY_404", "FAQ 대분류를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MAJOR_CATEGORY_DELETE_FAILED("ADMIN_FAQ_MAJOR_CATEGORY_400", "FAQ 대분류 삭제에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MAJOR_CATEGORY_DUPLICATE_NAME("ADMIN_FAQ_MAJOR_CATEGORY_409", "이미 존재하는 대분류 이름입니다.", HttpStatus.CONFLICT),

    // ===== [중분류 관련] =====
    MEDIUM_CATEGORY_NOT_FOUND("ADMIN_FAQ_MEDIUM_CATEGORY_404", "FAQ 중분류를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEDIUM_CATEGORY_DELETE_FAILED("ADMIN_FAQ_MEDIUM_CATEGORY_400", "FAQ 중분류 삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;
}
