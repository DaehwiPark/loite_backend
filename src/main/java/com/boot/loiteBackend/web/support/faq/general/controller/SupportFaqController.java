package com.boot.loiteBackend.web.support.faq.general.controller;

import com.boot.loiteBackend.web.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteBackend.web.support.faq.general.service.SupportFaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "고객센터 FAQ 목록 조회 API", description = "쇼핑몰 고객센터에서 사용하는 FAQ 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/support/faq")
public class SupportFaqController {

    private final SupportFaqService supportFaqService;

    @Operation(
            summary = "중분류 ID 기준 FAQ 목록 조회",
            description = "중분류 ID(mediumId)를 기준으로 FAQ 리스트를 조회합니다. 표시순서(faqOrder) 오름차순 정렬, 페이지네이션 포함"
    )
    @GetMapping("/list")
    public ResponseEntity<Page<SupportFaqDto>> getFaqListByMediumId(
            @Parameter(description = "중분류 카테고리 ID", example = "2", required = true)
            @RequestParam Long mediumId,

            @Parameter(description = "페이징 정보 (기본값: size=10, sort=faqOrder ASC)")
            @PageableDefault(size = 10, sort = "faqOrder", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<SupportFaqDto> result = supportFaqService.getFaqsByMediumCategory(mediumId, pageable);
        return ResponseEntity.ok(result);
    }

}
