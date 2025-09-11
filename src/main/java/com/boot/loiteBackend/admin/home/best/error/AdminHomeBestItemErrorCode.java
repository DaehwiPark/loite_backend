package com.boot.loiteBackend.admin.home.best.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminHomeBestItemErrorCode implements ErrorCode {

    INVALID_REQUEST("BEST_ITEM_001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("BEST_ITEM_002", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND("BEST_ITEM_003", "아이템을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUP_SLOT("BEST_ITEM_004", "해당 섹션에서 슬롯 번호가 중복됩니다.", HttpStatus.CONFLICT),
    SLOT_RANGE("BEST_ITEM_005", "슬롯은 1~10 범위여야 합니다.", HttpStatus.BAD_REQUEST),
    MAX_ITEMS("BEST_ITEM_006", "해당 섹션은 이미 10개로 가득 찼습니다.", HttpStatus.CONFLICT),
    SAVE_FAILED("BEST_ITEM_007", "저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_FAILED("BEST_ITEM_008", "삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
