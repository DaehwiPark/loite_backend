package com.boot.loiteMsBack.support.suggestion.controller;

import com.boot.loiteMsBack.support.suggestion.dto.SupportSuggestionDto;
import com.boot.loiteMsBack.support.suggestion.service.SupportSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/suggestion")
@Tag(name = "고객센터 경영진에게 제안 API", description = "고객센터 경영진에게 제안 내역 관련 API")
public class SupportSuggestionController {

    private final SupportSuggestionService suggestionService;

    @Operation(summary = "전체 제안 목록 조회")
    @GetMapping
    public ResponseEntity<List<SupportSuggestionDto>> getAllSuggestions() {
        List<SupportSuggestionDto> suggestions = suggestionService.getAllSuggestions();
        return ResponseEntity.ok(suggestions);
    }

    @Operation(summary = "단일 제안 조회")
    @GetMapping("/{id}")
    public ResponseEntity<SupportSuggestionDto> getSuggestionById(@PathVariable Long id) {
        SupportSuggestionDto suggestion = suggestionService.getSuggestionById(id);
        return ResponseEntity.ok(suggestion);
    }

    @Operation(summary = "제안 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long id) {
        suggestionService.deleteSuggestion(id);
        return ResponseEntity.noContent().build();
    }
}
