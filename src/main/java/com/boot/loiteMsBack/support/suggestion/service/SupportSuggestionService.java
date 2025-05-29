package com.boot.loiteMsBack.support.suggestion.service;

import com.boot.loiteMsBack.support.suggestion.dto.SupportSuggestionDto;

import java.util.List;

public interface SupportSuggestionService {
    List<SupportSuggestionDto> getAllSuggestions();
    SupportSuggestionDto getSuggestionById(Long id);
    void deleteSuggestion(Long id);
}
