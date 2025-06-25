package com.boot.loiteBackend.support.faq.category.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FaqCategoryErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 FAQ 카테고리를 찾을 수 없습니다."),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "이미 존재하는 카테고리 이름입니다."),
    DELETE_FAILED(HttpStatus.BAD_REQUEST, "삭제할 수 없는 카테고리입니다. 관련 FAQ가 존재할 수 있습니다."),
    UPDATE_FAILED(HttpStatus.BAD_REQUEST, "카테고리 수정에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
