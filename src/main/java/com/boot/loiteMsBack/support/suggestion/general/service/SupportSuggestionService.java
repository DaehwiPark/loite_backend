package com.boot.loiteMsBack.support.suggestion.general.service;

import com.boot.loiteMsBack.support.suggestion.general.dto.SupportSuggestionDto;
import com.boot.loiteMsBack.support.suggestion.general.dto.SupportSuggestionUpdateDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SupportSuggestionService {

    Page<SupportSuggestionDto> getPagedSuggestions(String keyword, Pageable pageable);

    SupportSuggestionDto getSuggestionById(Long id);

    void deleteSuggestion(Long id);

    void updateReviewStatus(Long id, SupportSuggestionUpdateDto updateDto);

    ResponseEntity<Resource> fileDownload(Long suggestionFileId);

}
