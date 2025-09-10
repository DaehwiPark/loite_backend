package com.boot.loiteBackend.web.home.recommend.section.error;

import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeRecoSectionErrorCode implements ErrorCode {

    RECO_SECTION_ERROR_CODE("RECO_SECTION_404", "추천 상품 섹션을 찾을 수 없습니다", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
