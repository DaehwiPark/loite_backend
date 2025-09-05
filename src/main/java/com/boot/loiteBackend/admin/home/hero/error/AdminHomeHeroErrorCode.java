package com.boot.loiteBackend.admin.home.hero.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminHomeHeroErrorCode implements ErrorCode {

    INVALID_REQUEST("HERO_000", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("HERO_999", "인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_FILE("HERO_001", "잘못된 파일입니다.", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED("HERO_002", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SAVE_FAILED("HERO_003", "히어로 섹션 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("HERO_004", "히어로 섹션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_DELETED("HERO_005", "이미 삭제된 히어로 섹션입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
