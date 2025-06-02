package com.boot.loiteMsBack.terms.controller;

import com.boot.loiteMsBack.terms.dto.TermsDto;
import com.boot.loiteMsBack.terms.service.TermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/terms")
@Tag(name = "이용약관 API", description = "이용약관 관련 API")
public class TermsController {

    private final TermsService termsService;

    @Operation(summary = "이용약관 등록", description = "새로운 이용약관을 등록합니다.")
    @PostMapping
    public ResponseEntity<TermsDto> createTerms(@RequestBody TermsDto dto) {
        TermsDto saved = termsService.createTerms(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "이용약관 수정", description = "기존 이용약관을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<TermsDto> updateTerms(@PathVariable Long id, @RequestBody TermsDto dto) {
        TermsDto updated = termsService.updateTerms(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "이용약관 삭제", description = "지정한 이용약관을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerms(@PathVariable Long id) {
        termsService.deleteTerms(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이용약관 전체 조회", description = "모든 이용약관 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<TermsDto>> getAllTerms() {
        return ResponseEntity.ok(termsService.getAllTerms());
    }

    @Operation(summary = "이용약관 단일 조회", description = "지정한 ID의 이용약관을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<TermsDto> getTermsById(@PathVariable Long id) {
        return ResponseEntity.ok(termsService.getTermsById(id));
    }
}
