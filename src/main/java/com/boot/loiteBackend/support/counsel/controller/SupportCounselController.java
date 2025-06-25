package com.boot.loiteBackend.support.counsel.controller;

import com.boot.loiteBackend.support.counsel.dto.SupportCounselDto;
import com.boot.loiteBackend.support.counsel.dto.SupportCounselReplyDto;
import com.boot.loiteBackend.support.counsel.dto.SupportCounselStatusUpdateDto;
import com.boot.loiteBackend.support.counsel.service.SupportCounselService;
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
@RequestMapping("/api/support/counsel")
@Tag(name = "고객센터 1:1 문의 API", description = "고객센터 1:1 게시판 문의 관련 API")
public class SupportCounselController {

    private final SupportCounselService supportCounselService;

    @Operation( summary = "전체 문의 목록 조회 (검색 & 페이지네이션)",description = "검색 키워드와 페이지 정보를 기반으로 관리자 또는 사용자 권한으로 전체 문의글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<SupportCounselDto>> getPagedCounsel(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<SupportCounselDto> result = supportCounselService.getPagedCounsel(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "미답변 문의 목록 조회 (검색 & 페이지네이션)", description = "답변이 등록되지 않은 문의만 필터링하여 페이지네이션 및 키워드 검색을 지원합니다.")
    @GetMapping("/unanswered")
    public ResponseEntity<Page<SupportCounselDto>> getUnansweredCounselPaged(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SupportCounselDto> result = supportCounselService.getUnansweredPagedCounsel(keyword, pageable);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "1:1 문의 단건 조회", description = "특정 ID에 해당하는 1:1 문의의 상세 내용을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportCounselDto> getCounselById(@PathVariable Long id) {
        return ResponseEntity.ok(supportCounselService.getCounselById(id));
    }


    @Operation(summary = "답변 등록", description = "관리자가 특정 문의에 대해 최초 답변을 등록합니다.")
    @PostMapping("/{id}/reply")
    public ResponseEntity<SupportCounselDto> addReply(
            @PathVariable Long id,
            @RequestBody SupportCounselReplyDto replyDto
    ) {
        return ResponseEntity.ok(supportCounselService.addReply(id, replyDto));
    }

    @Operation(summary = "답변 수정", description = "기존에 등록된 관리자 답변을 수정합니다.")
    @PutMapping("/{id}/reply")
    public ResponseEntity<SupportCounselDto> updateReply(
            @PathVariable Long id,
            @RequestBody SupportCounselReplyDto replyDto
    ) {
        return ResponseEntity.ok(supportCounselService.updateReply(id, replyDto));
    }
    @Operation(summary = "답변 삭제", description = "기존에 등록된 관리자 답변을 삭제하고 문의 상태를 대기로 변경합니다.")
    @DeleteMapping("/{id}/reply")
    public ResponseEntity<SupportCounselDto> deleteReply(@PathVariable Long id) {
        return ResponseEntity.ok(supportCounselService.deleteReply(id));
    }

    @Operation(summary = "문의 삭제 (소프트 삭제)", description = "DELETE_YN 컬럼을 'Y'로 설정하여 문의를 삭제 처리합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounsel(@PathVariable Long id) {
        supportCounselService.softDeleteCounsel(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문의 상태 변경", description = "문의 상태를 변경합니다. 예: 대기 → 완료 등.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<SupportCounselDto> updateStatus(
            @PathVariable Long id,
            @RequestBody SupportCounselStatusUpdateDto statusDto
    ) {
        return ResponseEntity.ok(supportCounselService.updateStatus(id, statusDto.getStatus()));
    }
}
