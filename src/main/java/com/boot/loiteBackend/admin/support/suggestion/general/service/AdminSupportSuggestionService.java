package com.boot.loiteBackend.admin.support.suggestion.general.service;

import com.boot.loiteBackend.admin.support.suggestion.general.dto.AdminSupportSuggestionDto;
import com.boot.loiteBackend.admin.support.suggestion.general.dto.AdminSupportSuggestionUpdateDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminSupportSuggestionService {

    Page<AdminSupportSuggestionDto> getPagedSuggestions(String keyword, Pageable pageable);

    AdminSupportSuggestionDto getSuggestionById(Long id);

    void deleteSuggestion(Long id);

    void updateReviewStatus(Long id, AdminSupportSuggestionUpdateDto updateDto);

    ResponseEntity<Resource> fileDownload(Long suggestionFileId);

}
