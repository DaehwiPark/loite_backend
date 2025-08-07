package com.boot.loiteBackend.web.cartItem.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CartItemErrorCode implements ErrorCode {

    SOLD_OUT_OPTION("CART_001", "품절된 옵션은 장바구니에 담을 수 없습니다.", HttpStatus.BAD_REQUEST),
    OPTION_NOT_FOUND("CART_002", "해당 옵션이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_OPTION_LIST("CART_003", "상품 옵션을 선택해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY("CART_004", "수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    GIFT_REQUIRED("CART_005", "사은품을 선택해주세요.", HttpStatus.BAD_REQUEST),
    GIFT_NOT_FOUND("CART_006", "선택한 사은품이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    SOLD_OUT_GIFT("CART_007", "품절된 사은품으로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND("CART_008", "장바구니 항목이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    EXCEED_GIFT_LIMIT("CART_009", "사은품은 상품 개수 만큼 담을 수 있습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}