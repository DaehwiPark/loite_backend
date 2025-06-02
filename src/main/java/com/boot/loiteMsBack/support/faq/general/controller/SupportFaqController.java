package com.boot.loiteMsBack.support.faq.general.controller;

import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.faq.general.dto.SupportFaqRequestDto;
import com.boot.loiteMsBack.support.faq.general.service.SupportFaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/faqs")
@Tag(name = "고객센터 자주 묻는 질문 API", description = "고객센터 자주 묻는 질문(FAQ) 관련 API")
public class SupportFaqController {

    private final SupportFaqService supportFaqService;

    @Operation(summary = "FAQ 전체 조회", description = "등록된 모든 FAQ 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<List<SupportFaqDto>> getAllFaq() {
        List<SupportFaqDto> faqs = supportFaqService.getAllFaq();
        return ResponseEntity.ok(faqs); // 200 OK
    }

    @Operation(summary = "FAQ 단건 조회", description = "주어진 ID에 해당 하는 FAQ 항목을 반환합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportFaqDto> getFaqById(@PathVariable Long id) {
        SupportFaqDto faq = supportFaqService.getFaqById(id);
        return ResponseEntity.ok(faq); // 200 OK
    }

    @Operation(summary = "FAQ 수정", description = "기존에 등록된 FAQ 내용을 수정합니다.")
    @PutMapping("/{id}/update")
    public ResponseEntity<SupportFaqDto> updateFaq(
            @PathVariable Long id,
            @RequestBody SupportFaqRequestDto request
    ) {
        SupportFaqDto updated = supportFaqService.updateFaq(id, request);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @Operation(summary = "FAQ 삭제", description = "FAQ 항목을 삭제(또는 삭제 처리)합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        supportFaqService.deleteFaqById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
