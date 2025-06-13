package com.boot.loiteMsBack.support.faq.general.error;

import com.boot.loiteMsBack.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum FaqErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 FAQ 항목을 찾을 수 없습니다."),
    DELETE_FAILED(HttpStatus.BAD_REQUEST, "FAQ 항목 삭제에 실패했습니다."),
    UPDATE_FAILED(HttpStatus.BAD_REQUEST, "FAQ 항목 수정에 실패했습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ 카테고리를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
