package com.boot.loiteBackend.support.notice.controller;

import com.boot.loiteBackend.support.notice.dto.SupportNoticeDto;
import com.boot.loiteBackend.support.notice.dto.SupportNoticeRequestDto;
import com.boot.loiteBackend.support.notice.service.SupportNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/notice")
@Tag(name = "공지사항 API", description = "공지사항 관련 API")
public class SupportNoticeController {

    private final SupportNoticeService supportNoticeService;

    @Operation(summary = "공지사항 등록", description = "새로운 공지사항을 등록합니다.")
    @PostMapping
    public ResponseEntity<SupportNoticeDto> createNotice(@RequestBody SupportNoticeRequestDto request) {
        SupportNoticeDto created = supportNoticeService.createNotice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "공지사항 수정", description = "기존 공지사항을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<SupportNoticeDto> updateNotice(@PathVariable Long id, @RequestBody SupportNoticeRequestDto request) {
        SupportNoticeDto updated = supportNoticeService.updateNotice(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제 처리합니다. (DEL_YN = 'Y')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        supportNoticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공지사항 전체 조회 (검색 및 페이징)", description = "제목 키워드로 검색하고, 페이지네이션 처리된 공지사항 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<SupportNoticeDto>> getPagedNotices(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SupportNoticeDto> result = supportNoticeService.getPagedNotices(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "공지사항 단일 조회", description = "ID로 공지사항 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportNoticeDto> getNoticeById(@PathVariable Long id) {
        return ResponseEntity.ok(supportNoticeService.getNoticeById(id));
    }
}
