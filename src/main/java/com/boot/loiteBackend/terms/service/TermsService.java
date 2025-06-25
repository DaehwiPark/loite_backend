package com.boot.loiteBackend.terms.service;

import com.boot.loiteBackend.terms.dto.TermsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermsService {

    TermsDto createTerms(TermsDto dto);

    TermsDto updateTerms(Long id, TermsDto dto);

    void deleteTerms(Long id);

    Page<TermsDto> getPagedTerms(String keyword, Pageable pageable);

    TermsDto getTermsById(Long id);
}
