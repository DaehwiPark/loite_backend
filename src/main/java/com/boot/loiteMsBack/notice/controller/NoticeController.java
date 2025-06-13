package com.boot.loiteMsBack.notice.controller;

import com.boot.loiteMsBack.notice.dto.NoticeDto;
import com.boot.loiteMsBack.notice.dto.NoticeRequestDto;
import com.boot.loiteMsBack.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notice")
@Tag(name = "공지사항 API", description = "공지사항 관련 API")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 등록", description = "새로운 공지사항을 등록합니다.")
    @PostMapping
    public ResponseEntity<NoticeDto> createNotice(@RequestBody NoticeRequestDto request) {
        NoticeDto created = noticeService.createNotice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "공지사항 수정", description = "기존 공지사항을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<NoticeDto> updateNotice(@PathVariable Long id, @RequestBody NoticeRequestDto request) {
        NoticeDto updated = noticeService.updateNotice(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제 처리합니다. (DEL_YN = 'Y')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공지사항 전체 조회", description = "삭제되지 않은 모든 공지사항 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @Operation(summary = "공지사항 단일 조회", description = "ID로 공지사항 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> getNoticeById(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNoticeById(id));
    }
}
