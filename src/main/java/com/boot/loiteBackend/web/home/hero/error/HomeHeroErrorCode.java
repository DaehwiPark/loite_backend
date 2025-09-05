package com.boot.loiteBackend.web.home.hero.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeHeroErrorCode implements ErrorCode {

    ACTIVE_NOT_FOUND("HOME_HERO_404", "활성 히어로가 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_LIMIT("HOME_HERO_400", "limit 값은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("HOME_HERO_500", "일시적인 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
