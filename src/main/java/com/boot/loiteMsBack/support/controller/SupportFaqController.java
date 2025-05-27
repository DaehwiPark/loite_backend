package com.boot.loiteMsBack.support.controller;

import com.boot.loiteMsBack.support.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support/faqs")
@Tag(name = "Support FAQ", description = "자주 묻는 질문(FAQ) 관련 API")
public class SupportFaqController {

    private final SupportService supportService;

    public SupportFaqController(SupportService supportService) {
        this.supportService = supportService;
    }

    /**
     * 전체 FAQ 조회
     * GET /api/support/faqs
     */
    @Operation(summary = "FAQ 전체 조회", description = "등록된 모든 FAQ 목록을 반환합니다.")
    @GetMapping("")
    public List<SupportFaqDto> getAllFaqs() {
        return supportService.getAllFaqDtos();
    }

    /**
     * 단건 FAQ 조회
     * GET /api/support/faqs/{id}
     */
    @Operation(summary = "FAQ 단건 조회", description = "주어진 ID에 해당하는 FAQ 항목을 반환합니다.")
    @GetMapping("/{id}")
    public SupportFaqDto getFaqById(@PathVariable Long id) {
        return supportService.getFaqDtoById(id);
    }

    /**
     * 미답변 FAQ 조회
     * GET /api/support/faqs/unanswered
     */
    @Operation(summary = "미답변 FAQ 조회", description = "답변이 아직 등록되지 않은 FAQ 목록을 조회합니다.")
    @GetMapping("/unanswered")
    public List<SupportFaqDto> getUnansweredFaqs() {
        return supportService.getUnansweredFaqDtos();
    }

    /**
     * 답변 등록
     * PUT /api/support/faqs/{id}/answer
     */
    @Operation(summary = "FAQ 답변 등록", description = "해당 FAQ 항목에 대한 답변을 등록합니다.")
    @PutMapping("/{id}/answer")
    public SupportFaqDto answerFaq(@PathVariable Long id, @RequestBody String answerContent) {
        return supportService.addAnswerToFaq(id, answerContent);
    }

    /**
     * 답변 수정
     * PUT /api/support/faqs/{id}/answer/edit
     */
    @Operation(summary = "FAQ 답변 수정", description = "기존에 등록된 답변 내용을 수정합니다.")
    @PutMapping("/{id}/answer/edit")
    public SupportFaqDto editAnswer(@PathVariable Long id, @RequestBody String updatedContent) {
        return supportService.updateFaqAnswer(id, updatedContent);
    }

    /**
     * 문의 삭제
     * DELETE /api/support/faqs/{id}
     */
    @Operation(summary = "FAQ 삭제", description = "FAQ 항목을 삭제(또는 삭제 처리)합니다.")
    @DeleteMapping("/{id}")
    public void deleteFaq(@PathVariable Long id) {
        supportService.deleteFaqById(id);
    }
}
