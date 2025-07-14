package com.boot.loiteBackend.web.support.notice.controller;

import com.boot.loiteBackend.web.support.notice.dto.SupportNoticeDto;
import com.boot.loiteBackend.web.support.notice.service.SupportNoticeService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/support/notice")
@Tag(name = "웹 공지사항 API", description = "쇼핑몰 사용자를 위한 공지사항 조회 API")
public class SupportNoticeController {

    private final SupportNoticeService supportNoticeService;

    @Operation(summary = "공지사항 전체 조회 (검색 및 페이징)", description =
            "공지사항 제목을 키워드로 검색하고, 페이징 처리된 공지사항 목록을 반환합니다."
            + "기본 페이지 크기는 10개이며, 최신 등록일 기준으로 내림차순 정렬됩니다."
    )
    @GetMapping
    public ResponseEntity<Page<SupportNoticeDto>> getPagedNotices(
            @Parameter(description = "검색 키워드 (제목 기준)") @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(supportNoticeService.getPagedNotices(keyword, pageable));
    }

    @Operation(summary = "공지사항 조회수 증가", description = "공지사항 ID에 해당하는 항목의 조회수를 1 증가시킵니다.")
    @PatchMapping("/{id}/views")
    public ResponseEntity<Void> insertViewCount(
            @Parameter(description = "공지사항 ID") @PathVariable Long id
    ) {
        supportNoticeService.insertViewCount(id);
        return ResponseEntity.ok().build();
    }
}
