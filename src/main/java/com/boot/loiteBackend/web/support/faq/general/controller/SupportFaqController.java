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

@Tag(name = "고객센터 FAQ 관련 API", description = "FAQ 대분류/중분류 카테고리 및 목록 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/support/faq")
public class SupportFaqController {

    private final SupportFaqService supportFaqService;

    @Operation(
            summary = "FAQ 목록 전체 조회(중분류 ID 기준)",
            description = "FAQ 리스트를 조회합니다. 표시순서(faqOrder) 오름차순 정렬, 페이지네이션 포함"
    )
    @GetMapping
    public ResponseEntity<Page<SupportFaqDto>> getFaqListByMediumId(
            @Parameter(description = "중분류 카테고리 ID", example = "2", required = true)
            @RequestParam Long mediumId,
            @Parameter(description = "페이징 정보 (기본값: size=10, sort=faqOrder ASC)")
            @PageableDefault(size = 10, sort = "faqOrder", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<SupportFaqDto> result = supportFaqService.getFaqsByMediumCategory(mediumId, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "FAQ 목록 전체 조회(대분류 ID 기준)",
            description = "중분류들의 FAQ를 모두 조회합니다. 표시순서 기준 정렬, 페이지네이션 포함"
    )
    @GetMapping("/all")
    public ResponseEntity<Page<SupportFaqDto>> getFaqsByMajorId(
            @Parameter(description = "대분류 ID", example = "1", required = true)
            @RequestParam Long majorId,
            @Parameter(description = "페이징 정보 (기본값: size=10, sort=faqOrder ASC)")
            @PageableDefault(size = 10, sort = "faqOrder", direction = Sort.Direction.ASC) Pageable pageable,
            @Parameter(description = "검색어")
            @RequestParam(required = false) String keyword
    ) {
        Page<SupportFaqDto> result = supportFaqService.getFaqsByMajorCategory(majorId, pageable, keyword);
        return ResponseEntity.ok(result);
    }
}
