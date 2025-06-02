package com.boot.loiteMsBack.support.counsel.controller;

import com.boot.loiteMsBack.support.counsel.dto.SupportCounselDto;
import com.boot.loiteMsBack.support.counsel.dto.SupportCounselReplyDto;
import com.boot.loiteMsBack.support.counsel.dto.SupportCounselStatusUpdateDto;
import com.boot.loiteMsBack.support.counsel.service.SupportCounselService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/counsel")
@Tag(name = "고객센터 1:1 문의 API", description = "고객센터 1:1 게시판 문의 관련 API")
public class SupportCounselController {

    private final SupportCounselService supportCounselService;

    @Operation(summary = "전체 문의 목록 조회", description = "관리자 또는 사용자 권한으로 전체 문의글 목록을 조회합니다. 삭제된 항목은 제외됩니다.")
    @GetMapping
    public ResponseEntity<List<SupportCounselDto>> getAllCounsel() {
        return ResponseEntity.ok(supportCounselService.getAllCounsel());
    }

    @Operation(summary = "1:1 문의 단건 조회", description = "특정 ID에 해당하는 1:1 문의의 상세 내용을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportCounselDto> getCounselById(@PathVariable Long id) {
        return ResponseEntity.ok(supportCounselService.getCounselById(id));
    }

    @Operation(summary = "미답변 문의 조회", description = "아직 답변이 등록되지 않은 상태의 문의 목록을 조회합니다.")
    @GetMapping("/unanswered")
    public ResponseEntity<List<SupportCounselDto>> getUnansweredCounsel() {
        return ResponseEntity.ok(supportCounselService.getUnansweredCounsel());
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

    @Operation(summary = "문의 삭제 (소프트 삭제)", description = "DEL_YN 컬럼을 'Y'로 설정하여 문의를 삭제 처리합니다.")
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
