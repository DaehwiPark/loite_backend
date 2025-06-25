package com.boot.loiteBackend.support.suggestion.general.controller;

import com.boot.loiteBackend.support.suggestion.general.dto.SupportSuggestionDto;
import com.boot.loiteBackend.support.suggestion.general.dto.SupportSuggestionUpdateDto;
import com.boot.loiteBackend.support.suggestion.general.service.SupportSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/suggestion")
@Tag(name = "고객센터 경영진에게 제안 API", description = "고객센터 경영진에게 제안 내역을 조회, 수정, 삭제, 다운로드할 수 있는 API입니다.")
public class SupportSuggestionController {

    private final SupportSuggestionService supportSuggestionService;

    @Operation(summary = "제안 목록 페이징 조회", description = "삭제되지 않은 제안 목록을 페이지 단위로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<SupportSuggestionDto>> getPagedSuggestions(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        Page<SupportSuggestionDto> result = supportSuggestionService.getPagedSuggestions(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "자료 상세 조회", description = "제안 ID를 통해 단일 제안의 상세 내용을 조회합니다. 첨부파일 및 작성자 정보 포함.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportSuggestionDto> getSuggestionById(@PathVariable Long id) {
        SupportSuggestionDto suggestion = supportSuggestionService.getSuggestionById(id);
        return ResponseEntity.ok(suggestion);
    }

    @Operation(summary = "제안 삭제", description = "제안 ID를 기준으로 해당 제안을 삭제 처리합니다. (실제 삭제가 아닌 deleteYn='Y' 처리)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long id) {
        supportSuggestionService.deleteSuggestion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "검토 상태 수정", description = "제안의 검토 상태(검토대기/검토중/검토완료) 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReviewAndReplyStatus(
            @PathVariable Long id,
            @RequestBody SupportSuggestionUpdateDto updateDto) {
        supportSuggestionService.updateReviewStatus(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "첨부파일 다운로드", description = "첨부파일 ID를 통해 서버에 저장된 파일을 다운로드합니다. 실제 파일 경로를 사용하여 다운로드 응답을 생성합니다.")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long suggestionFileId) {
        return supportSuggestionService.fileDownload(suggestionFileId);
    }
}
